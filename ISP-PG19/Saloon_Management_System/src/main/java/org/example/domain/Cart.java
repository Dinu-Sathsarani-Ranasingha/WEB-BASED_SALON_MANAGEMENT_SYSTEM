package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cart")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private Long cartId;

    // Link this to the Customer entity from User Management
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "Status")
    private String status = "ACTIVE";

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}