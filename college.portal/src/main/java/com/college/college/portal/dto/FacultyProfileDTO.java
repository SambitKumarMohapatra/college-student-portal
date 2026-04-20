package com.college.college.portal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class FacultyProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String designation;
    private String department;
    private String qualification;
    private Integer experienceYears;
    private String role;
    private LocalDateTime createdAt;
}
