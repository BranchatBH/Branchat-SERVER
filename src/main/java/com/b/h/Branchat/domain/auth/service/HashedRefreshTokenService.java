package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.entity.HashedRefreshToken;
import com.b.h.Branchat.domain.auth.exception.AuthErrorCode;
import com.b.h.Branchat.domain.auth.exception.AuthException;
import com.b.h.Branchat.domain.auth.repository.HashedRefreshTokenRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashedRefreshTokenService {
    private final HashedRefreshTokenRepository hashedRefreshTokenRepository;

    @Transactional
    public void saveRefreshTokenAsHash(String refreshToken, UUID memberId) {
        if(refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_NULL);
        }
        String hashedToken = hashToken(refreshToken);
        HashedRefreshToken token = HashedRefreshToken.builder()
            .hashedRefreshToken(hashedToken)
            .memberId(memberId.toString())
            .build();
        hashedRefreshTokenRepository.save(token);
    }

    @Transactional
    public void removeRefreshToken(String refreshToken) {
        if(refreshToken == null || refreshToken.isBlank()) {
            return;
        }
        String hashedToken = hashToken(refreshToken);
        hashedRefreshTokenRepository.findById(hashedToken).ifPresent(hashedRefreshTokenRepository::delete);
    }

    private String hashToken(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthException(AuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
