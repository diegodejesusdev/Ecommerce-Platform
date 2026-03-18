package com.ecommerce.service;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("john@example.com");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.00"));

        cart = new Cart(user);
        cart.setId(100L);
        cart.setItems(new ArrayList<>());
    }

    @Test
    void getOrCreateCartForUser_shouldReturnExistingCart() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCartForUser("john@example.com");

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void getOrCreateCartForUser_shouldCreateCartWhenMissing() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.getOrCreateCartForUser("john@example.com");

        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void calculateCartTotal_shouldReturnZeroForNullCart() {
        assertEquals(BigDecimal.ZERO, cartService.calculateCartTotal(null));
    }

    @Test
    void calculateCartTotal_shouldSumAllItems() {
        CartItem item1 = new CartItem(product, 2);
        CartItem item2 = new CartItem(product, 3);

        cart.getItems().add(item1);
        cart.getItems().add(item2);

        BigDecimal total = cartService.calculateCartTotal(cart);

        assertEquals(new BigDecimal("50.00"), total);
    }

    @Test
    void addItemToCart_shouldAddNewItemWhenNotExisting() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());

        cartService.addItemToCart("john@example.com", 1L, 2);

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(cartCaptor.capture());

        Cart savedCart = cartCaptor.getValue();
        assertNotNull(savedCart);
        assertFalse(savedCart.getItems().isEmpty());
    }

    @Test
    void addItemToCart_shouldIncreaseQuantityWhenItemExists() {
        CartItem existingItem = new CartItem(product, 2);
        existingItem.setCart(cart);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(existingItem));

        cartService.addItemToCart("john@example.com", 1L, 3);

        assertEquals(5, existingItem.getQuantity());
        verify(cartItemRepository).save(existingItem);
    }

    @Test
    void removeItemFromCart_shouldRemoveItemWhenCartMatches() {
        CartItem item = new CartItem(product, 2);
        item.setId(10L);
        item.setCart(cart);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(item));

        cartService.removeItemFromCart("john@example.com", 10L);

        verify(cartRepository).save(cart);
    }

    @Test
    void updateQuantity_shouldRemoveItemWhenQuantityIsZero() {
        CartItem item = new CartItem(product, 2);
        item.setId(10L);
        item.setCart(cart);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(item));

        doNothing().when(cartItemRepository).delete(item);

        cartService.updateQuantity("john@example.com", 10L, 0);

        verify(cartRepository).save(cart);
    }

    @Test
    void updateQuantity_shouldSetNewQuantity() {
        CartItem item = new CartItem(product, 2);
        item.setId(10L);
        item.setCart(cart);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(item));

        cartService.updateQuantity("john@example.com", 10L, 5);

        assertEquals(5, item.getQuantity());
        verify(cartItemRepository).save(item);
    }
}
