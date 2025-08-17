package com.b.h.Branchat.domain.user.entity;

import com.b.h.Branchat.domain.user.enums.ProviderType;
import com.b.h.Branchat.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

public class MemberIdentity extends BaseEntity {
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
}
