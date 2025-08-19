package com.b.h.Branchat.domain.auth.repository;

import com.b.h.Branchat.domain.auth.enums.ProviderType;
import com.b.h.Branchat.domain.auth.entity.AuthProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthProviderRepository
    extends JpaRepository<AuthProvider, Long>, AuthProviderRepositoryCustom {

    Optional<AuthProvider> findByProviderAndProviderUserId(ProviderType providerType, String sub);
}

