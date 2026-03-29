package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Order;
import com.ecommerce.payment.PaymentProvider;
import com.ecommerce.payment.PaymentRequest;
import com.ecommerce.payment.PaymentResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.PaymentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, CartService cartService, PaymentService paymentService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    @GetMapping("/checkout")
    public String showCheckout(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            Cart cart = cartService.getOrCreateCartForUser(userDetails.getUsername());
            model.addAttribute("cart", cart);
        }
        model.addAttribute("paymentProviders", paymentService.getAllProviders());
        model.addAttribute("pageTitle", "Checkout");
        return "orders/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam String shippingAddress,
                                  @RequestParam(defaultValue = "CASH_ON_DELIVERY") String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login to place an order.");
            return "redirect:/login";
        }

        try {
            PaymentProvider provider = PaymentProvider.valueOf(paymentMethod);
            Order order = orderService.createOrderFromCart(userDetails.getUsername(), shippingAddress);

            // Process payment
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .orderNumber(order.getOrderNumber())
                    .amount(order.getTotal())
                    .currency("USD")
                    .customerEmail(userDetails.getUsername())
                    .customerName(order.getUser().getFullName())
                    .description("Order " + order.getOrderNumber())
                    .build();

            PaymentResponse response = paymentService.processPayment(provider, paymentRequest);

            if (!response.isSuccess()) {
                // Cancel order if payment fails
                orderService.cancelOrder(order.getId());
                redirectAttributes.addFlashAttribute("errorMessage",
                    "Payment failed: " + response.getErrorMessage());
                return "redirect:/orders/checkout";
            }

            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
            redirectAttributes.addFlashAttribute("orderNumber", order.getOrderNumber());
            redirectAttributes.addFlashAttribute("transactionId", response.getTransactionId());
            return "redirect:/orders/" + order.getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/orders/checkout";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid payment method selected.");
            return "redirect:/orders/checkout";
        }
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("pageTitle", "Order " + order.getOrderNumber());
        return "orders/detail";
    }

    @PostMapping("/{id}/confirm")
    public String confirmOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.confirmOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order confirmed!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order cancelled.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/ship")
    public String shipOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.shipOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order shipped!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/deliver")
    public String deliverOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deliverOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order delivered!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @GetMapping("/list")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("pageTitle", "All Orders");
        return "orders/list";
    }

    @GetMapping("/my-orders")
    public String myOrders(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        var ordersPage = orderService.getUserOrders(userDetails.getUsername(), page, 10);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("pageTitle", "My Orders");
        return "orders/my-orders";
    }

    @GetMapping("/{id}/user")
    public String userOrderDetail(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Order order = orderService.getOrderById(id);
        // Verify the order belongs to the user
        if (!order.getUser().getEmail().equals(userDetails.getUsername())) {
            return "redirect:/orders/my-orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("pageTitle", "Order " + order.getOrderNumber());
        return "orders/detail";
    }
}
