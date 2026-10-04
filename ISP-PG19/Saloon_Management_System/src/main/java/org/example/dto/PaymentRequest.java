package org.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PaymentRequest {
    public String cardNumber; // Should be 16 digits
    public String expiryDate; // Should be MM/YY
    public String cvv;        // Should be 3 digits
}
