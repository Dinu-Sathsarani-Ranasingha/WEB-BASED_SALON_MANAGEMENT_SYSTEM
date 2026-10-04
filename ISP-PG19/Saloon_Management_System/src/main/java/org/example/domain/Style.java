package org.example.domain;
// ════════════════════════════════════════════════════════════════════════════
// Style.java
// ════════════════════════════════════════════════════════════════════════════
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "style")
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long styleId;

    @NotBlank(message = "Style number is required")
    private String styleNo;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be under 500 characters")
    private String description;

    @NotBlank(message = "Type is required")
    private String type;

    // Add this inside your Style class, along with its getter and setter

    @Column(name = "customer_id")
    private Long customerId;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getStyleId() { return styleId; }
    public void setStyleId(Long styleId) { this.styleId = styleId; }

    public String getStyleNo() { return styleNo; }
    public void setStyleNo(String styleNo) { this.styleNo = styleNo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
