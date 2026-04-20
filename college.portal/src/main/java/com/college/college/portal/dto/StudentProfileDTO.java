package com.college.college.portal.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class StudentProfileDTO {
    private Long id;
    private String rollNumber;
    private String name;
    private String email;
    private String phone;
    private String branch;
    private Integer semester;
    private Integer enrollmentYear;
    private LocalDate dob;
    private String address;
    private String city;
    private String pincode;
    private String role;
    private LocalDateTime createdAt;

}
