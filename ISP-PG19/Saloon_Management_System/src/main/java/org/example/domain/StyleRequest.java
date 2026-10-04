package org.example.domain;

import jakarta.validation.constraints.NotBlank;

public class StyleRequest {

    @NotBlank(message = "Hair type is required")
    private String hairType;

    @NotBlank(message = "Face shape is required")
    private String faceShape;

    @NotBlank(message = "Skin tone is required")
    private String skinTone;

    @NotBlank(message = "Eye color is required")
    private String eyeColor;

    @NotBlank(message = "Style preference is required")
    private String stylePreference;

    public String getHairType() { return hairType; }
    public void setHairType(String hairType) { this.hairType = hairType; }

    public String getFaceShape() { return faceShape; }
    public void setFaceShape(String faceShape) { this.faceShape = faceShape; }

    public String getSkinTone() { return skinTone; }
    public void setSkinTone(String skinTone) { this.skinTone = skinTone; }

    public String getEyeColor() { return eyeColor; }
    public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }

    public String getStylePreference() { return stylePreference; }
    public void setStylePreference(String stylePreference) { this.stylePreference = stylePreference; }
}