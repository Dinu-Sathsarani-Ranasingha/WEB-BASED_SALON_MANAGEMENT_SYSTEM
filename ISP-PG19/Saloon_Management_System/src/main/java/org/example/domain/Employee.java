package org.example.domain;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "First name can only contain letters and spaces")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Last name can only contain letters and spaces")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary cannot be negative")
    private Double salary;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String accStatus = "ACTIVE";

    private LocalDate hireDate;

    @NotNull(message = "Work start time is required")
    @DateTimeFormat(pattern = "HH:mm") // <-- YOU NEED THIS HERE
    private LocalTime workStartTime;

    @NotNull(message = "Work end time is required")
    @DateTimeFormat(pattern = "HH:mm") // <-- AND THIS HERE
    private LocalTime workEndTime;

    public Employee() {}
}