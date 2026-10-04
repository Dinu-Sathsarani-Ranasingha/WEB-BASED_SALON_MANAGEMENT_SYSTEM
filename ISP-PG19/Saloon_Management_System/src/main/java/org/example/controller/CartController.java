package org.example.controller;

import org.example.domain.Cart;
import org.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. Initialize a new cart (for when a customer first arrives)
    @PostMapping("/init")
    public ResponseEntity<Cart> initCart() {
        // Passing null forces the service to create and save a brand new cart
        return ResponseEntity.ok(cartService.getOrCreateCart(null));
    }

    // 2. Get the current cart details
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getOrCreateCart(cartId));
    }

    // 3. Add a Service Style to the cart
    @PostMapping("/{cartId}/addStyle")
    public ResponseEntity<Cart> addStyleToCart(
            @PathVariable Long cartId,
            @RequestParam Long styleId,
            @RequestParam(defaultValue = "1") Integer quantity) {

        Cart updatedCart = cartService.addStyleToCart(cartId, styleId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // 4. Remove an item from the cart
    @DeleteMapping("/{cartId}/remove/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        Cart updatedCart = cartService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok(updatedCart);
    }
}