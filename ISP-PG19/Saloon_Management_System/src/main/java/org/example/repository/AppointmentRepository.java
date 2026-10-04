package org.example.repository;

import org.example.domain.Appointment;
import org.example.domain.AppointmentStatus;
import org.example.dto.MonthlyRevenueDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT new org.example.dto.MonthlyRevenueDTO(MONTH(a.appointmentDate), SUM(a.totalPrice)) " +
            "FROM Appointment a " +
            "WHERE YEAR(a.appointmentDate) = :year AND a.status = :status " +
            "GROUP BY MONTH(a.appointmentDate) " +
            "ORDER BY MONTH(a.appointmentDate)")
    List<MonthlyRevenueDTO> getMonthlyRevenueForYear(@Param("year") int year, @Param("status") AppointmentStatus status);

    // Used to disable buttons on the frontend calendar
    List<Appointment> findByAppointmentDateAndStatusNot(LocalDate date, AppointmentStatus status);

    // Used for the customer history page
    List<Appointment> findByCustomerPhoneOrderByAppointmentDateDesc(String customerPhone);

    // NEW: Used for the Daily Appointments Owner Dashboard
    List<Appointment> findByAppointmentDateOrderByAppointmentTimeAsc(LocalDate appointmentDate);

    List<Appointment> findByAppointmentDateAndAssignedStaffEmployeeIdOrderByAppointmentTimeAsc(LocalDate date, Long staffId);

    // Add this new method
// Note: If the ID field inside your Customer entity is just 'id' instead of 'customerId',
// change the method name to findByCustomer_IdOrderByAppointmentDateDesc
    List<Appointment> findByCustomer_CustomerIdOrderByAppointmentDateDesc(Long customerId);

    // Count today's tasks (excluding cancelled ones) for the assigned staff member
    long countByAppointmentDateAndAssignedStaffEmployeeIdAndStatusNot(LocalDate date, Long staffId, AppointmentStatus status);

    // Count all historical completed (PAID) services for the assigned staff member
    long countByAssignedStaffEmployeeIdAndStatus(Long staffId, AppointmentStatus status);
}