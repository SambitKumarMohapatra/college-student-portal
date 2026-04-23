package com.college.college.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class TokenRefreshResponseDTO {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
}
