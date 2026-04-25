package com.college.college.portal.service;

import com.college.college.portal.entity.TokenBlacklist;
import com.college.college.portal.repository.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Transactional
    public void blacklistToken(String token, Instant expiresAt) {
        try {
            if (!tokenBlacklistRepository.existsByToken(token)) {
                tokenBlacklistRepository.save(
                        TokenBlacklist.builder()
                                .token(token)
                                .expiresAt(expiresAt)
                                .build()
                );
            }
        } catch (Exception e) {
            log.error("Error blacklisting token: {}", e.getMessage(), e);
        }
    }

    public boolean isBlacklisted(String token) {
        try {
            return tokenBlacklistRepository.existsByToken(token);
        } catch (Exception e) {
            log.error("Error checking blacklist: {}", e.getMessage(), e);
            return false;
        }
    }

    // Auto-cleanup expired blacklisted tokens every hour
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            tokenBlacklistRepository.deleteExpiredTokens(Instant.now());
            log.info("Cleaned up expired blacklisted tokens");
        } catch (Exception e) {
            log.error("Cleanup task failed: {}", e.getMessage());

        }
    }
}
