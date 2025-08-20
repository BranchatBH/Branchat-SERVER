package com.b.h.Branchat.domain.auth.entity;

import com.b.h.Branchat.domain.auth.enums.ProviderType;
import com.b.h.Branchat.domain.user.entity.Member;
import com.b.h.Branchat.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
@Entity
public class AuthProvider extends BaseEntity {
    private static final int MAX_PROVIDER_LENGTH = 10;
    private static final int MAX_PROVIDER_USER_ID_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = MAX_PROVIDER_LENGTH, nullable = false)
    private ProviderType provider;

    @Column(name = "provider_user_id", length = MAX_PROVIDER_USER_ID_LENGTH, nullable = false)
    private String providerUserId;

    public static AuthProvider create(Member member, ProviderType provider, String providerUserId) {
        return AuthProvider.builder().member(member).provider(provider)
            .providerUserId(providerUserId).build();
    }
}
