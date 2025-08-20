package com.b.h.Branchat.domain.auth.repository;

import com.b.h.Branchat.domain.auth.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByJwtRefreshToken(String refreshToken);
}
