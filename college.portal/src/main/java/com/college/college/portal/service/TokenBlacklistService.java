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

    private static final TokenBlacklistRepository tokenBlacklistRepository = null;

    @Transactional
    public static void blacklistToken(String token, Instant expiresAt) {
        // Avoid duplicates
        if (!tokenBlacklistRepository.existsByToken(token)) {
            tokenBlacklistRepository.save(
                    TokenBlacklist.builder()
                            .token(token)
                            .expiresAt(expiresAt)
                            .build()
            );
        }
    }

    public boolean isBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }

    // Auto-cleanup expired blacklisted tokens every hour
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredTokens() {
        tokenBlacklistRepository.deleteExpiredTokens(Instant.now());
        log.info("Cleaned up expired blacklisted tokens");
    }
}
