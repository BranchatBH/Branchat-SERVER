package com.b.h.Branchat.domain.member.service;

import static com.b.h.Branchat.domain.member.exception.MemberErrorCode.USER_NOT_FOUND;

import com.b.h.Branchat.domain.member.dto.response.MemberInfoResponse;
import com.b.h.Branchat.domain.member.entity.Member;
import com.b.h.Branchat.domain.member.exception.MemberException;
import com.b.h.Branchat.domain.member.repository.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberInfoResponse getUserInfo(UUID memberId) {
        Member user = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(USER_NOT_FOUND));

        return MemberInfoResponse.from(user);
    }
}
