package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Entity
@Table(name = "package_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name cannot be empty")
    @Column(name = "ItemName", nullable = false, length = 150)
    private String name;

    @NotNull(message = "Item price cannot be empty")
    @Positive(message = "Item price must be greater than zero")
    @Column(name = "ItemPrice", precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "Availability", length = 20)
    private String availability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PackageID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PackageCategory packageCategory;
}