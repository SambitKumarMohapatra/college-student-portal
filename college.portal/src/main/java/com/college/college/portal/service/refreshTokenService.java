package com.college.college.portal.service;

import com.college.college.portal.entity.RefreshToken;
import com.college.college.portal.entity.Role;
import com.college.college.portal.exception.TokenException;
import com.college.college.portal.repository.AdminRepository;
import com.college.college.portal.repository.FacultyRepository;
import com.college.college.portal.repository.RefreshTokenRepository;
import com.college.college.portal.repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class refreshTokenService {
    @Value("${app.jwt.refresh-expiration-ms:604800000}") // 7 days default
    private long refreshTokenDurationMs;

    private static final RefreshTokenRepository refreshTokenRepository = null;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public RefreshToken createRefreshToken(String email) {
        // Delete existing refresh token for this user if present
        refreshTokenRepository.findByUserEmail(email)
                .ifPresent(refreshTokenRepository::delete);

        Role role = resolveRole(email);

        RefreshToken token = RefreshToken.builder()
                .userEmail(email)
                .userRole(role)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(token);
    }

    public static RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenException("Refresh token expired. Please login again.");
        }
        return token;
    }

    public static RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException("Invalid refresh token."));
    }

    @Transactional
    public static void deleteByEmail(String email) {
        refreshTokenRepository.deleteByUserEmail(email);
    }

    private Role resolveRole(String email) {
        if (studentRepository.findByEmail(email).isPresent()) return Role.STUDENT;
        if (facultyRepository.findByEmail(email).isPresent()) return Role.FACULTY;
        if (adminRepository.findByEmail(email).isPresent()) return Role.ADMIN;
        throw new RuntimeException("User not found for email: " + email);
    }
}
