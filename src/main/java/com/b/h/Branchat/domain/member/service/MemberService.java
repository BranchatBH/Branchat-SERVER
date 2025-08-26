package com.b.h.Branchat.domain.member.service;

import static com.b.h.Branchat.domain.member.exception.MemberErrorCode.USER_NOT_FOUND;

import com.b.h.Branchat.domain.auth.repository.AuthProviderRepository;
import com.b.h.Branchat.domain.auth.repository.HashedRefreshTokenRepository;
import com.b.h.Branchat.domain.member.dto.response.MemberInfoResponse;
import com.b.h.Branchat.domain.member.entity.Member;
import com.b.h.Branchat.domain.member.exception.MemberException;
import com.b.h.Branchat.domain.member.repository.MemberRepository;
import com.b.h.Branchat.domain.node.repository.NodeRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthProviderRepository authProviderRepository;
    private final NodeRepository nodeRepository;
    private final HashedRefreshTokenRepository hashedRefreshTokenRepository;

    public MemberInfoResponse getUserInfo(UUID memberId) {
        Member user = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(USER_NOT_FOUND));

        return MemberInfoResponse.from(user);
    }

    @Transactional
    public void deleteMember(UUID memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberException(USER_NOT_FOUND);
        }

        authProviderRepository.deleteAllByMemberId(memberId);
        nodeRepository.deleteAllByMemberId(memberId);
        hashedRefreshTokenRepository.deleteByMemberId(memberId.toString());
        memberRepository.deleteById(memberId);
    }
}