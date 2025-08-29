package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.infra.client.oauth.dto.GoogleUserInfo;
import com.b.h.Branchat.domain.auth.enums.ProviderType;
import com.b.h.Branchat.domain.auth.repository.AuthProviderRepository;
import com.b.h.Branchat.domain.member.entity.Member;
import com.b.h.Branchat.domain.member.repository.MemberRepository;
import com.b.h.Branchat.domain.auth.entity.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthProvisioningService {

    private final MemberRepository memberRepository;
    private final AuthProviderRepository authProviderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member createMemberAndProvider(GoogleUserInfo googleUserInfo) {
        // 회원 정보를 저장합니다.
        Member newMember = createMember(googleUserInfo);
        // OAuth 제공자 정보를 저장하고, Member와 연결합니다.
        createAuthProvider(newMember, googleUserInfo);
        return newMember;
    }

    private Member createMember(GoogleUserInfo googleUserInfo) {
        Member member = Member.create(googleUserInfo.name(), googleUserInfo.email(),
            googleUserInfo.profileImageUrl());
        memberRepository.save(member);
        return member;
    }

    private void createAuthProvider(Member member, GoogleUserInfo googleUserInfo) {
        AuthProvider authProvider = AuthProvider.create(member, ProviderType.GOOGLE,
            googleUserInfo.sub());
        authProviderRepository.save(authProvider);
    }
}
