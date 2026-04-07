package com.college.college.portal.dto;

import lombok.*;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class JwtResponseDTO {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private String role;

}
