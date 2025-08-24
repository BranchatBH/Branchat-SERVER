package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.entity.HashedRefreshToken;
import com.b.h.Branchat.domain.auth.exception.AuthErrorCode;
import com.b.h.Branchat.domain.auth.exception.AuthException;
import com.b.h.Branchat.domain.auth.repository.HashedRefreshTokenRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashedRefreshTokenService {
    private final HashedRefreshTokenRepository hashedRefreshTokenRepository;
    private final TokenHasher tokenHasher;

    @Transactional
    public void saveRefreshTokenAsHash(String refreshToken, UUID memberId) {
        if(refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_NULL);
        }
        String hashedToken = tokenHasher.hash(refreshToken);
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
        String hashedToken = tokenHasher.hash(refreshToken);
        hashedRefreshTokenRepository.findById(hashedToken).ifPresent(hashedRefreshTokenRepository::delete);
    }

    public void validateRefreshToken(String refreshToken, UUID memberId) {
        String hashedToken = tokenHasher.hash(refreshToken);
        HashedRefreshToken hashedRefreshToken = hashedRefreshTokenRepository.findById(hashedToken)
            .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));
        if (!hashedRefreshToken.getMemberId().equals(memberId.toString())) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISMATCH);
        }
    }
}
