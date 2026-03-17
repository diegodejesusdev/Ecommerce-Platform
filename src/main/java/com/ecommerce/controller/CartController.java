package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            Cart cart = cartService.getOrCreateCartForUser(userDetails.getUsername());
            model.addAttribute("cart", cart);
        }
        model.addAttribute("pageTitle", "Cart");
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity) {
        if (userDetails != null) {
            cartService.addItemToCart(userDetails.getUsername(), productId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam Long itemId) {
        if (userDetails != null) {
            cartService.removeItemFromCart(userDetails.getUsername(), itemId);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam Long itemId,
                                 @RequestParam int quantity) {
        if (userDetails != null) {
            cartService.updateQuantity(userDetails.getUsername(), itemId, quantity);
        }
        return "redirect:/cart";
    }
}
