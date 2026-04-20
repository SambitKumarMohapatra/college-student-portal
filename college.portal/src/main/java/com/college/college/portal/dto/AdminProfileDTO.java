package com.college.college.portal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class AdminProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String adminRole;
    private String role;
    private LocalDateTime createdAt;
}
