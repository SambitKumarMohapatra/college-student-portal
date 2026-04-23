package com.college.college.portal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("LMS Portal — Password Reset Request");
            message.setText(
                    "Hello,\n\n" +
                            "You requested a password reset for your LMS account.\n\n" +
                            "Use this token to reset your password:\n\n" +
                            "Token: " + resetToken + "\n\n" +
                            "Send a POST request to: /api/auth/reset-password\n" +
                            "Body: { \"token\": \"" + resetToken + "\", \"newPassword\": \"YourNewPass@123\" }\n\n" +
                            "This token expires in 15 minutes.\n\n" +
                            "If you did not request this, ignore this email.\n\n" +
                            "— Landmine Soft LMS Team"
            );
            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send reset email. Please try again.");
        }
    }
}
