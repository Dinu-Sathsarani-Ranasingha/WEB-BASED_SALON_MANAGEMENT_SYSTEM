package org.example.service;

import org.example.domain.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create staff / receptionist / owner account
    public void createEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setAccStatus("ACTIVE");
        employee.setHireDate(LocalDate.now());
        employeeRepository.save(employee);
    }

    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username).orElse(null);
    }
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public void activateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            employee.setAccStatus("ACTIVE");
            employeeRepository.save(employee);
        }
    }

    public void deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            employee.setAccStatus("INACTIVE");
            employeeRepository.save(employee);
        }
    }
    public List<Employee> searchByKeyword(String keyword) {
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, keyword, keyword);
    } //sprint3

    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }


}

