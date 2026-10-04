package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Cart_Item")
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartItemID")
    private Long cartItemId;

    @Column(name = "Quantity")
    private Integer quantity = 1;

    // The Link back to the Cart
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CartID", nullable = false)
    private Cart cart;

    // OPTION A: The item is a Service Style
    @ManyToOne
    @JoinColumn(name = "StyleID", nullable = true) // Nullable because it might be a package!
    private ServiceStyle serviceStyle;

    // Add this to your existing CartItem.java class
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_category_id", nullable = true)
    // It is nullable=true because a cart item might be a Service instead of a Package!
    private PackageCategory packageCategory;

    // OPTION B: The item is a Package (Your teammate's entity)
    // Uncomment and adjust this once your teammate pushes the Package class!
    /*
    @ManyToOne
    @JoinColumn(name = "PackageID", nullable = true)
    private SalonPackage salonPackage;
    */

    // Helper method to calculate the price of this specific line
    public Double getSubtotal() {
        if (serviceStyle != null) {
            return serviceStyle.getPrice() * quantity;
        }
        // else if (salonPackage != null) { return salonPackage.getPrice() * quantity; }
        return 0.0;
    }
}