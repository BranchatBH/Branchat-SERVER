package com.b.h.Branchat.domain.member.dto.response;

import com.b.h.Branchat.domain.member.entity.Member;
import java.util.UUID;

public record MemberInfoResponse(
    UUID id,
    String name,
    String email,
    String profileImageUrl
) {

    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getProfileImageUrl());
    }
}
