package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.client.GoogleOAuthClient;
import com.b.h.Branchat.domain.auth.dto.response.AuthResults;
import com.b.h.Branchat.domain.auth.dto.response.CheckMember;
import com.b.h.Branchat.domain.auth.dto.response.GoogleTokenResponse;
import com.b.h.Branchat.domain.auth.dto.response.GoogleUserInfo;
import com.b.h.Branchat.domain.user.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final List<String> ALLOWED_REDIRECT_URIS = List.of(
        "http://localhost:5173/auth/google/callback",
        "https://<your-extension-id>.chromiumapp.org/"
    );
    private final GoogleOAuthClient googleOAuthClient;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthProviderService authProviderService;

    @Value("${GOOGLE_REDIRECT_URI}")
    String defaultRedirectUri;

    public AuthResults loginOrSignupWithCode(
        String code, String codeVerifier, String clientRedirectUri
    ){
        String effectiveRedirectUri = resolveRedirectUri(clientRedirectUri); //유효한 redirect uri인지 검증

        GoogleUserInfo googleUserInfo = getGoogleUserInfoFromGoogle(code, effectiveRedirectUri, codeVerifier); //구글에서 유저 정보 가져옴

        CheckMember checkMember = authProviderService.findOrCreateMember(googleUserInfo); //우리 회원인지 확인

        Member member = checkMember.member(); //액세스 토큰, 리프레시 토큰 생성
        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        refreshTokenService.saveRefreshToken(refreshToken, member.getId()); //리프레시 토큰 redis에 저장

        return new AuthResults(accessToken, refreshToken, checkMember.isNewMember());
    }

    private GoogleUserInfo getGoogleUserInfoFromGoogle(String code, String effectiveRedirectUri, String codeVerifier) {
        GoogleTokenResponse tokens = googleOAuthClient.getGoogleToken(code, effectiveRedirectUri, codeVerifier);
        return googleOAuthClient.getGoogleUserInfo(tokens.accessToken());
    }

    private String resolveRedirectUri(String clientRedirectUri) {
        if (clientRedirectUri != null && ALLOWED_REDIRECT_URIS.contains(clientRedirectUri)) {
            return clientRedirectUri;
        }
        return this.defaultRedirectUri;   // 기본값(배포용) 사용
    }
}
