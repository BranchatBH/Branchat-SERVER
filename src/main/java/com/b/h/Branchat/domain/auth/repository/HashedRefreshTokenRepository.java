package com.b.h.Branchat.domain.auth.repository;

import com.b.h.Branchat.domain.auth.entity.HashedRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface HashedRefreshTokenRepository extends CrudRepository<HashedRefreshToken, String> {
}
