package com.college.college.portal.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class FacultyRegisterDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be 8+ chars with 1 uppercase, 1 number, 1 special character"
    )
    private String password;

    @NotBlank(message = "Department is required")
    @Pattern(regexp = "^(CSE|ECE|ME|CE|EE)$", message = "Department must be one of: CSE, ECE, ME, CE, EE")
    private String department;

    @NotBlank(message = "Designation is required")
    @Pattern(
            regexp = "^(Professor|Associate Prof|Assistant Prof)$",
            message = "Designation must be: Professor, Associate Prof, or Assistant Prof"
    )
    private String designation;

    @Size(max = 100)
    private String qualification;

    @Min(0) @Max(60)
    private Integer experienceYears;
}
