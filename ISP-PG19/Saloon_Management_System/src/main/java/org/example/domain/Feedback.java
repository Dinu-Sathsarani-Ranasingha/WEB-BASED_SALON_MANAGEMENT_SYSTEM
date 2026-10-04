package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long feedbackId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Comment is required")
    @Size(min = 5, max = 1000, message = "Comment must be between 5 and 1000 characters")
    private String comment;

    // Optional — no @NotBlank
    private String service;

    // Optional — no @NotBlank
    private String staffMember;

    private LocalDate date;
    private Long customerId;

    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getStaffMember() { return staffMember; }
    public void setStaffMember(String staffMember) { this.staffMember = staffMember; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}