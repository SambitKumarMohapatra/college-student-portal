package com.college.college.portal.dto;

import lombok.*;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class ApiResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String message;
}
