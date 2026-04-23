package com.college.college.portal.service;

import com.college.college.portal.entity.PasswordResetToken;
import com.college.college.portal.exception.ResourceNotFoundException;
import com.college.college.portal.exception.TokenException;
import com.college.college.portal.repository.AdminRepository;
import com.college.college.portal.repository.FacultyRepository;
import com.college.college.portal.repository.PasswordResetTokenRepository;
import com.college.college.portal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository resetTokenRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final long EXPIRY_MINUTES = 15;

    // ── Forgot Password ───────────────────────────────────────
    @Transactional
    public void processForgotPassword(String email) {
        // Check email exists in any table
        boolean exists = studentRepository.existsByEmail(email)
                || facultyRepository.existsByEmail(email)
                || adminRepository.existsByEmail(email);

        if (!exists) {
            throw new ResourceNotFoundException("No account found with email: " + email);
        }

        // Delete any existing token for this email
        resetTokenRepository.deleteByUserEmail(email);

        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .userEmail(email)
                .expiryDate(Instant.now().plusSeconds(EXPIRY_MINUTES * 60))
                .used(false)
                .build();

        resetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(email, token);
    }

    // ── Reset Password ────────────────────────────────────────
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException("Invalid or expired reset token."));

        if (resetToken.isUsed()) {
            throw new TokenException("Reset token already used.");
        }
        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            resetTokenRepository.delete(resetToken);
            throw new TokenException("Reset token has expired. Please request a new one.");
        }

        String email = resetToken.getUserEmail();
        String hashed = passwordEncoder.encode(newPassword);

        // Update password in whichever table has this email
        studentRepository.findByEmail(email).ifPresent(s -> {
            s.setPasswordHash(hashed);
            studentRepository.save(s);
        });
        facultyRepository.findByEmail(email).ifPresent(f -> {
            f.setPasswordHash(hashed);
            facultyRepository.save(f);
        });
        adminRepository.findByEmail(email).ifPresent(a -> {
            a.setPasswordHash(hashed);
            adminRepository.save(a);
        });

        // Mark token as used
        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }
}
