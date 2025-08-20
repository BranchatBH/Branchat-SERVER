package com.b.h.Branchat.domain.user.entity;

import com.b.h.Branchat.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    private static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = MAX_NAME_LENGTH, nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_image_url", columnDefinition = "TEXT", nullable = false)
    private String profileImageUrl;

    public static Member create(String name, String email, String profileImageUrl) {
        return Member.builder()
            .name(name)
            .email(email)
            .profileImageUrl(profileImageUrl)
            .build();
    }
}
