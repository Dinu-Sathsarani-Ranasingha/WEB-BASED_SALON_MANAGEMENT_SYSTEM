package org.example.domain;

public enum AppointmentStatus {
    PENDING,    // The appointment is created, but not yet paid
    PAID,       // The appointment is confirmed and paid
    CANCELLED   // The appointment was cancelled by the customer
}