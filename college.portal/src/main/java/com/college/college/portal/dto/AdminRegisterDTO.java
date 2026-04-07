package com.college.college.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class AdminRegisterDTO {
    @NotBlank(message = "Name is required")
    @Size(min=2,max=100)
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

    @NotBlank(message = "Role is required")
    @Pattern(
            regexp = "^(SuperAdmin|Admin|Accountant)$",
            message = "Role must be: SuperAdmin, Admin, or Accountant"
    )
    private String role;
}
