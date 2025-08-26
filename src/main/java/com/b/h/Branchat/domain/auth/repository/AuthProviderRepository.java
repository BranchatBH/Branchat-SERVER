package com.b.h.Branchat.domain.auth.repository;

import com.b.h.Branchat.domain.auth.enums.ProviderType;
import com.b.h.Branchat.domain.auth.entity.AuthProvider;
import com.b.h.Branchat.domain.member.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuthProviderRepository
    extends JpaRepository<AuthProvider, UUID>, AuthProviderRepositoryCustom {

    Optional<AuthProvider> findByProviderAndProviderUserId(ProviderType providerType, String sub);

    @Modifying
    @Transactional
    @Query("delete from AuthProvider ap where ap.member = :member")
    long deleteAllByMember(@Param("member") Member member);

    @Modifying
    @Transactional
    @Query("delete from AuthProvider ap where ap.member.id = :memberId")
    long deleteAllByMemberId(@Param("memberId") UUID memberId);
}
