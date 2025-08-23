package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.entity.RefreshToken;
import com.b.h.Branchat.domain.auth.repository.RefreshTokenRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(String refreshToken, UUID memberId) {
        RefreshToken token = RefreshToken.builder()
            .jwtRefreshToken(refreshToken)
            .memberId(memberId.toString())
            .build();
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void removeRefreshToken(String refreshToken) {
        refreshTokenRepository.findRefreshTokenByJwtRefreshToken(refreshToken)
            .ifPresent(refreshTokenRepository::delete);
    }
}
