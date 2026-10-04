package org.example.service;
import org.example.domain.Customer;
import org.example.domain.Employee;
import org.example.domain.Role;
import org.example.repository.CustomerRepository;
import org.example.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {
//core login-loading logic in Spring Security
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserActivityService userActivityService;

    public CustomUserDetailsService(CustomerRepository customerRepository,
                                    EmployeeRepository employeeRepository,
                                    UserActivityService userActivityService) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.userActivityService = userActivityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Check Employee table
        Employee employee = employeeRepository.findByUsername(username).orElse(null);
        if (employee != null) {
            if (!"ACTIVE".equals(employee.getAccStatus())) {
                throw new UsernameNotFoundException("Account is inactive.");
            }

            // Track login
            try {
                String fullName = employee.getFirstName() + " " + employee.getLastName();
                userActivityService.trackLogin(username, employee.getRole().name(), fullName);
            } catch (Exception e) { logger.error("Tracking error", e); }

            // RETURN CUSTOM OBJECT WITH employeeId
            return new CustomUserDetails(
                    employee.getEmployeeId(),
                    employee.getUsername(),
                    employee.getPassword(),
                    employee.getRole().name()
            );
        }

        // 2. Check Customer table
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!"ACTIVE".equals(customer.getAccStatus())) {
            throw new UsernameNotFoundException("Account is inactive.");
        }

        // Track login
        try {
            userActivityService.trackLogin(username, "CUSTOMER", customer.getName());
        } catch (Exception e) { logger.error("Tracking error", e); }

        // RETURN CUSTOM OBJECT WITH customerId
        return new CustomUserDetails(
                customer.getCustomerId(),
                customer.getUsername(),
                customer.getPassword(),
                "CUSTOMER"
        );
    }
}
