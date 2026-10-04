package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "customer_features")
public class CustomerFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;

    @NotBlank(message = "Face shape is required")
    private String faceShape;

    @NotBlank(message = "Eye color is required")
    private String eyeColor;

    @NotBlank(message = "Skin tone is required")
    private String skinTone;

    @NotBlank(message = "Hair type is required")
    private String hairType;

    @NotBlank(message = "Style preference is required")
    private String stylePreference;

    private Long customerId;

    // Getters and Setters
    public Long getFeatureId() { return featureId; }
    public void setFeatureId(Long featureId) { this.featureId = featureId; }

    public String getFaceShape() { return faceShape; }
    public void setFaceShape(String faceShape) { this.faceShape = faceShape; }

    public String getEyeColor() { return eyeColor; }
    public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }

    public String getSkinTone() { return skinTone; }
    public void setSkinTone(String skinTone) { this.skinTone = skinTone; }

    public String getHairType() { return hairType; }
    public void setHairType(String hairType) { this.hairType = hairType; }

    public String getStylePreference() { return stylePreference; }
    public void setStylePreference(String stylePreference) { this.stylePreference = stylePreference; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}

