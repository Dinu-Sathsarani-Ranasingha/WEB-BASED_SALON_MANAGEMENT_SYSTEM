package org.example.domain;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "Salon_Service")
@Getter // Lombok automatically creates all getters
@Setter // Lombok automatically creates all setters
public class SalonService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SalonServiceID")
    private Long salonServiceId;

    @NotBlank(message = "Service name cannot be empty")
    @Column(name = "Service_Name")
    private String serviceName;

    // Many Salon Services belong to One Category
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "CategoryID", nullable = false)
    private ServiceCategory serviceCategory;

    // One Salon Service has Many Styles
    @OneToMany(mappedBy = "salonService", cascade = CascadeType.ALL)
    private List<ServiceStyle> serviceStyles;
}