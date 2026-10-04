package org.example.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest {

    @NotBlank(message = "Customer name is required")
    public String customerName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    public String customerPhone;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    public LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    public LocalTime appointmentTime;

    @NotNull(message = "Total price is required")
    @PositiveOrZero(message = "Total price cannot be negative")
    public Double totalPrice;

    public String bookedItemsJson;

    @NotNull(message = "Stylist selection is required")
    public Long staffId;
}