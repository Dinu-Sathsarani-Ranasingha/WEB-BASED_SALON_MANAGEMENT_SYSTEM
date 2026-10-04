package org.example.service;

import org.example.domain.Cart;
import org.example.domain.CartItem;
import org.example.domain.ServiceStyle;
import org.example.repository.CartRepository;
import org.example.repository.CartItemRepository;
import org.example.repository.ServiceStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private ServiceStyleRepository styleRepo;

    // 1. Get or Create a Cart (Simulating a user's active session)
    public Cart getOrCreateCart(Long cartId) {
        if (cartId != null && cartRepo.existsById(cartId)) {
            return cartRepo.findById(cartId).orElseThrow();
        }
        Cart newCart = new Cart();
        return cartRepo.save(newCart);
    }

    // 2. Add a Style to the Cart
    @Transactional
    public Cart addStyleToCart(Long cartId, Long styleId, Integer quantity) {
        Cart cart = getOrCreateCart(cartId);
        ServiceStyle style = styleRepo.findById(styleId)
                .orElseThrow(() -> new RuntimeException("Style not found"));

        // Check if this style is already in the cart!
        for (CartItem item : cart.getItems()) {
            if (item.getServiceStyle() != null && item.getServiceStyle().getStyleId().equals(styleId)) {
                // If it is, just increase the quantity
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepo.save(item);
                return cart;
            }
        }

        // If it's a new item, create it
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setServiceStyle(style);
        newItem.setQuantity(quantity);

        cart.getItems().add(newItem);
        return cartRepo.save(cart);
    }

    // 3. Remove an item from the cart
    @Transactional
    public Cart removeItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow();
        cart.getItems().removeIf(item -> item.getCartItemId().equals(cartItemId));
        return cartRepo.save(cart); // orphanRemoval=true will delete the CartItem from the DB
    }
}
