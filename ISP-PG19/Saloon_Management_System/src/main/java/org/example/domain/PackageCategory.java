package org.example.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "package_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PackageID")
    private Long id;

    @NotBlank(message = "Package name cannot be empty")
    @Column(name = "PackageCategoryName", nullable = false, length = 120)
    private String name;

    @NotBlank(message = "Package description cannot be empty")
    @Column(name = "PackageDescription", nullable = false, length = 1000)
    private String description;

    @Column(name = "Package_type", length = 50)
    private String packageType;

    @Column(name = "PackageStatus", length = 30)
    private String status;

    @NotNull(message = "Total price cannot be empty")
    @PositiveOrZero(message = "Total price cannot be negative")
    @Column(name = "PackageTotalPrice", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "Updated_date")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "packageCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PackageItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    public void addItem(PackageItem item) {
        this.items.add(item);
        item.setPackageCategory(this);
    }

    public BigDecimal calculateTotalFromItems() {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        return items.stream()
                .map(PackageItem::getPrice)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}