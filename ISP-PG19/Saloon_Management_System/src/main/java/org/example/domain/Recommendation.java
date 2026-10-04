package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "recommendation")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be under 500 characters")
    private String description;

    @NotBlank(message = "Type is required")
    private String type;

    private Long customerId;

    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long id) { this.recommendationId = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}
