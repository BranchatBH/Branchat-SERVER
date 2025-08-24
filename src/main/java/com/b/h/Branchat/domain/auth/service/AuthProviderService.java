package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.dto.response.CheckMember;
import com.b.h.Branchat.domain.auth.dto.response.GoogleUserInfo;
import com.b.h.Branchat.domain.auth.entity.AuthProvider;
import com.b.h.Branchat.domain.auth.enums.ProviderType;
import com.b.h.Branchat.domain.auth.repository.AuthProviderRepository;
import com.b.h.Branchat.domain.member.entity.Member;
import com.b.h.Branchat.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthProviderService {

    private final AuthProviderRepository authProviderRepository;
    private final MemberRepository memberRepository;
    private final AuthProvisioningService authProvisioningService;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CheckMember findOrCreateMember(GoogleUserInfo googleUserInfo) {
        // DB 조회는 기본적으로 쓰기 트랜잭션 내에서 수행해도 무방
        Optional<AuthProvider> optionalProvider = authProviderRepository.findByProviderAndProviderUserId(
            ProviderType.GOOGLE, googleUserInfo.sub());

        //로그인 유저일 경우 유저 정보 전달
        if (optionalProvider.isPresent()) {
            Member member = optionalProvider.get().getMember();
            return new CheckMember(member, false); // 기존 멤버 반환
        }

        //회원가입 필요할 경우 유저 정보 db에 저장후 전달
        try {
            Member newMember = authProvisioningService.createMemberAndProvider(googleUserInfo);
            return new CheckMember(newMember, true);
        }catch (DataIntegrityViolationException e) {
            return authProviderRepository
                .findByProviderAndProviderUserId(ProviderType.GOOGLE, googleUserInfo.sub())
                .map(p -> new CheckMember(p.getMember(), false))
                .orElseThrow(() -> e);
        }
    }

    private void createAuthProvider(Member member, GoogleUserInfo googleUserInfo) {
        AuthProvider authProvider = AuthProvider.create(member, ProviderType.GOOGLE,
            googleUserInfo.sub());
        authProviderRepository.save(authProvider);
    }

    private Member createMember(GoogleUserInfo googleUserInfo) {
        Member member = Member.create(googleUserInfo.name(), googleUserInfo.email(),
            googleUserInfo.profileImageUrl());
        memberRepository.save(member);
        return member;
    }
}
