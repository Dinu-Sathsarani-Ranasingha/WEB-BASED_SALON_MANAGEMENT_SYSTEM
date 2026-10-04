package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import jakarta.persistence.Column;

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @Min(value = 1, message = "Please select a rating")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank
    private String service;

    @NotBlank
    private String staffMember;

    private LocalDate date;
    private Long customerId;

    @NotBlank(message = "Name is required")
    @Column(nullable = true)
    private String name;

    public Long getRatingId() { return ratingId; }
    public void setRatingId(Long ratingId) { this.ratingId = ratingId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getStaffMember() { return staffMember; }
    public void setStaffMember(String staffMember) { this.staffMember = staffMember; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
