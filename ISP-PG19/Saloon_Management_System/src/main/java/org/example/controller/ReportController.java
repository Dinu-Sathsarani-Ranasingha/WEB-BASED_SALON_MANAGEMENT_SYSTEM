package org.example.controller;

import org.example.domain.AppointmentStatus;
import org.example.dto.MonthlyRevenueDTO;
import org.example.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/revenue/{year}")
    public ResponseEntity<List<MonthlyRevenueDTO>> getRevenueReport(@PathVariable int year) {
        // Assuming 'PAID' or 'COMPLETED' is the enum value for successful checkouts
        List<MonthlyRevenueDTO> reportData = appointmentRepository.getMonthlyRevenueForYear(year, AppointmentStatus.PAID);
        return ResponseEntity.ok(reportData);
    }
}
