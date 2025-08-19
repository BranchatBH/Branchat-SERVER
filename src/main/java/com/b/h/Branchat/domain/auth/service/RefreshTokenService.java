package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.user.repository.MemberRepository;
import com.b.h.Branchat.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final MemberRepository memberRepository;

    public void createAndStoreRefreshToken(UUID memberId, HttpServletResponse response) {
        String refreshToken = jwtProvider.createRefreshToken(memberId);
        //ㄴ토큰을 만들어
        cookieUtil.createRefreshTokenCookie(response, refreshToken);
        //ㄴ쿠키에 담아
        addRefreshTokenToDB(memberId, refreshToken);
        //ㄴdb에 refresh token 추가
    }

}
