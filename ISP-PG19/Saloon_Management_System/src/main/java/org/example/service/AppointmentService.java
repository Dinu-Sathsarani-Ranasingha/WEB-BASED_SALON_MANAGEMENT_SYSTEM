package org.example.service;

import org.example.dto.*;
import org.example.domain.*;
import org.example.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private org.example.repository.EmployeeRepository employeeRepository;

    @Autowired
    private org.example.repository.CustomerRepository customerRepository;

    // 1. Get Booked Times (for the Calendar)
    public List<LocalTime> getBookedTimes(LocalDate date) {
        return repository.findByAppointmentDateAndStatusNot(date, AppointmentStatus.CANCELLED)
                .stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toList());
    }

    // 2. Create Appointment
    public Appointment createAppointment(AppointmentRequest req, Long customerId) {
        if (getBookedTimes(req.appointmentDate).contains(req.appointmentTime)) {
            throw new RuntimeException("This time slot is already booked. Please select another.");
        }

        Appointment appt = new Appointment();
        appt.setCustomerName(req.customerName);
        appt.setCustomerPhone(req.customerPhone);
        appt.setAppointmentDate(req.appointmentDate);
        appt.setAppointmentTime(req.appointmentTime);
        appt.setTotalPrice(req.totalPrice);
        appt.setBookedItemsJson(req.bookedItemsJson);
        appt.setStatus(AppointmentStatus.PENDING);

        // NEW: Link the logged-in customer to the appointment
        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            appt.setCustomer(customer);
        }

        // Link the staff member
        if (req.staffId != null) {
            Employee staff = employeeRepository.findById(req.staffId)
                    .orElseThrow(() -> new RuntimeException("Stylist not found"));
            appt.setAssignedStaff(staff);
        }

        return repository.save(appt);
    }

    // 3. Process Mock Payment with Validations
    public Appointment processPayment(Long id, PaymentRequest payment) {
        Appointment appt = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Validations
        if (payment.cardNumber == null || !payment.cardNumber.matches("\\d{16}")) {
            throw new RuntimeException("Invalid Card Number: Must be 16 digits.");
        }
        if (payment.expiryDate == null || !payment.expiryDate.matches("(0[1-9]|1[0-2])\\/\\d{2}")) {
            throw new RuntimeException("Invalid Expiry Date: Must be MM/YY format.");
        }
        if (payment.cvv == null || !payment.cvv.matches("\\d{3}")) {
            throw new RuntimeException("Invalid CVV: Must be 3 digits.");
        }

        // Mask the card for history view (e.g., **** **** **** 1234)
        String masked = "**** **** **** " + payment.cardNumber.substring(12);
        appt.setMaskedCardNumber(masked);
        appt.setStatus(AppointmentStatus.PAID);

        return repository.save(appt);
    }

    // 4. Reschedule Appointment
    public Appointment reschedule(Long id, RescheduleRequest req) {
        Appointment appt = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appt.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Cannot reschedule a cancelled appointment.");
        }

        if (getBookedTimes(req.newDate).contains(req.newTime)) {
            throw new RuntimeException("The requested new time slot is already booked.");
        }

        appt.setAppointmentDate(req.newDate);
        appt.setAppointmentTime(req.newTime);
        return repository.save(appt);
    }

    // 5. Cancel Appointment
    public Appointment cancelAppointment(Long id) {
        Appointment appt = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appt.setStatus(AppointmentStatus.CANCELLED);
        return repository.save(appt);
    }

    // 6. Get Customer History
    public List<Appointment> getHistory(Long customerId) {
        return repository.findByCustomer_CustomerIdOrderByAppointmentDateDesc(customerId);
    }

    // 7. Get Daily Appointments for Owner Dashboard
    public List<Appointment> findByDate(String dateString, Long staffId) {
        LocalDate date = LocalDate.parse(dateString);
        if (staffId != null) {
            return repository.findByAppointmentDateAndAssignedStaffEmployeeIdOrderByAppointmentTimeAsc(date, staffId);
        }
        return repository.findByAppointmentDateOrderByAppointmentTimeAsc(date);
    }

    // Get the count for Today's Tasks
    public long getTodayTaskCount(Long staffId) {
        return repository.countByAppointmentDateAndAssignedStaffEmployeeIdAndStatusNot(
                LocalDate.now(), staffId, AppointmentStatus.CANCELLED);
    }

    // Get the total count of Services Done (Paid/Completed)
    public long getCompletedServicesCount(Long staffId) {
        return repository.countByAssignedStaffEmployeeIdAndStatus(
                staffId, AppointmentStatus.PAID);
    }


}
