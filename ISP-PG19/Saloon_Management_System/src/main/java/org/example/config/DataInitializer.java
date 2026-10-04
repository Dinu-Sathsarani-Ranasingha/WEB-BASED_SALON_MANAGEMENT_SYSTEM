package org.example.config;

import org.example.domain.Employee;
import org.example.domain.Role;
import org.example.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeService employeeService;

    public DataInitializer(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void run(String... args) {
        if (employeeService.findByUsername("admin") == null) {
            Employee owner = new Employee();
            owner.setFirstName("System");
            owner.setLastName("Admin");
            owner.setUsername("admin");
            owner.setEmail("admin@salon.lk");
            owner.setPassword("admin123");
            owner.setRole(Role.OWNER);

            // --- New required fields to prevent startup errors ---
            owner.setSalary(0.0);
            owner.setWorkStartTime(LocalTime.of(8, 0)); // 08:00 AM
            owner.setWorkEndTime(LocalTime.of(17, 0));   // 05:00 PM
            // ----------------------------------------------------

            employeeService.createEmployee(owner);
            System.out.println("Default OWNER account created: username=admin / password=admin123");
        }
    }
}