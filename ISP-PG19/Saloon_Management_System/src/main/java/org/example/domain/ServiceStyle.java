package org.example.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Service_Style")
@Getter
@Setter
public class ServiceStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StyleID")
    private Long styleId;

    @NotBlank(message = "Style name cannot be empty")
    @Column(name = "Style_Name")
    private String styleName;

    // CHANGED: @NotBlank to @NotNull for the Double type
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be greater than zero")
    @Column(name = "Price")
    private Double price;

    @Column(name = "Availability")
    private Boolean availability;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "SalonServiceID", nullable = false)
    private SalonService salonService;
}