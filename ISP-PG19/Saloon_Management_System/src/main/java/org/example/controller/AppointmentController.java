package org.example.controller;

import jakarta.validation.Valid; // <-- Make sure to import this!
import org.example.dto.*;
import org.example.domain.Appointment;
import org.example.service.AppointmentService;
import org.example.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @GetMapping("/booked-times")
    public ResponseEntity<List<LocalTime>> getBookedTimes(@RequestParam String date) {
        return ResponseEntity.ok(service.getBookedTimes(LocalDate.parse(date)));
    }

    // --- ADDED @Valid HERE ---
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(
            @Valid @RequestBody AppointmentRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long customerId = userDetails.getCustomerId();
            return ResponseEntity.ok(service.createAppointment(req, customerId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> payAppointment(@PathVariable Long id, @RequestBody PaymentRequest req) {
        try {
            return ResponseEntity.ok(service.processPayment(id, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<?> reschedule(@PathVariable Long id, @RequestBody RescheduleRequest req) {
        try {
            return ResponseEntity.ok(service.reschedule(id, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelAppointment(id));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Appointment>> getHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();
        return ResponseEntity.ok(service.getHistory(customerId));
    }

    @GetMapping("/daily")
    public ResponseEntity<List<Appointment>> getDailyAppointments(
            @RequestParam String date,
            @RequestParam(required = false) Long staffId) {
        return ResponseEntity.ok(service.findByDate(date, staffId));
    }
}