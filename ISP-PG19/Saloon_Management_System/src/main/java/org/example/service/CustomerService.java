package org.example.service;

import org.example.domain.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Validation for registration
    public Map<String, String> validateRegistration(Customer customer) {
        Map<String, String> errors = new HashMap<>();

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            errors.put("name", "Full name is required");
        } else {
            String nameTrim = customer.getName().trim();
            if (!nameTrim.matches("^[A-Za-z\\s]+$")) {
                errors.put("name", "Name can only contain letters and spaces");
            }
            if (nameTrim.length() < 2 || nameTrim.length() > 100) {
                errors.put("name", "Name must be between 2 and 100 characters");
            }
        }

        if (customer.getUsername() == null || customer.getUsername().trim().isEmpty()) {
            errors.put("username", "Username is required");
        } else {
            String usernameTrim = customer.getUsername().trim();
            if (usernameTrim.length() < 4 || usernameTrim.length() > 50) {
                errors.put("username", "Username must be between 4 and 50 characters");
            }
            Optional<Customer> existing = customerRepository.findByUsername(usernameTrim);
            if (existing.isPresent()) {
                errors.put("username", "Username is already taken");
            }
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            errors.put("email", "Email is required");
        } else {
            String emailTrim = customer.getEmail().trim();
            if (!emailTrim.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                errors.put("email", "Please enter a valid email address");
            }
        }

        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
            errors.put("phoneNumber", "Phone number is required");
        } else {
            String phone = customer.getPhoneNumber().trim().replaceAll("\\s+", "");
            if (!phone.matches("^\\d{10}$")) {
                errors.put("phoneNumber", "Phone number must be exactly 10 digits (0-9 only)");
            }
        }

        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            errors.put("password", "Password is required");
        } else if (customer.getPassword().length() < 6) {
            errors.put("password", "Password must be at least 6 characters long");
        }

        return errors;
    }

    public void registerCustomer(Customer customer) {
        customer.setName(customer.getName() != null ? customer.getName().trim() : "");
        customer.setUsername(customer.getUsername() != null ? customer.getUsername().trim() : "");
        customer.setEmail(customer.getEmail() != null ? customer.getEmail().trim() : "");
        customer.setPhoneNumber(customer.getPhoneNumber() != null ? customer.getPhoneNumber().trim() : "");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRegistrationDate(LocalDate.now());
        customer.setAccStatus("ACTIVE");

        customerRepository.save(customer);
    }

    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username).orElse(null);
    } //Read

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    // Update
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setAccStatus("ACTIVE");
            customerRepository.save(customer);
        }
    }

    public void deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setAccStatus("INACTIVE");
            customerRepository.save(customer);
        }
    }
    public List<Customer> searchByKeyword(String keyword) {
        return customerRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, keyword);
    }

}