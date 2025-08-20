package com.b.h.Branchat.domain.auth.dto.response;

import com.b.h.Branchat.domain.user.entity.Member;

public record CheckMember(Member member, boolean isNewMember) {

}
