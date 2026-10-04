package org.example.controller;

import org.example.domain.Cart;
import org.example.domain.CartItem;
import org.example.domain.PackageCategory;
import org.example.domain.ServiceStyle;

import org.example.dto.CartRequest;
import org.example.repository.CartRepository;
import org.example.repository.CartItemRepository;
import org.example.repository.PackageCategoryRepository;
import org.example.repository.ServiceStyleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private PackageCategoryRepository packageRepository;

    @Autowired
    private ServiceStyleRepository styleRepository;

    @PostMapping("/checkout")
    public ResponseEntity<?> processCheckout(@RequestBody CartRequest cartRequest) {

        System.out.println("Processing checkout to database...");

        // 1. Create and save the main Cart record
        Cart cart = new Cart();
        // Assuming your Cart entity has a setTotalAmount method:
        // cart.setTotalAmount(cartRequest.getTotalAmount());
        cart = cartRepository.save(cart);

        // 2. Save all individual Services (Styles) to cart_item table
        if (cartRequest.getServiceIds() != null) {
            for (Long styleId : cartRequest.getServiceIds()) {
                CartItem item = new CartItem();
                item.setCart(cart);
                item.setServiceStyle(styleRepository.findById(styleId).orElse(null));
                item.setQuantity(1);
                cartItemRepository.save(item);
            }
        }

        // 3. Save all Packages to cart_item table
        if (cartRequest.getPackageIds() != null) {
            for (Long pkgId : cartRequest.getPackageIds()) {
                CartItem item = new CartItem();
                item.setCart(cart);
                item.setPackageCategory(packageRepository.findById(pkgId).orElse(null));
                item.setQuantity(1);
                cartItemRepository.save(item);
            }
        }

        return ResponseEntity.ok("Successfully saved to database!");
    }
}
