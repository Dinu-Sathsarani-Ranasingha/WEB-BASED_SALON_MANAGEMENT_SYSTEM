package org.example.domain;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeEdit {

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "First name can only contain letters and spaces")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Last name can only contain letters and spaces")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
}
