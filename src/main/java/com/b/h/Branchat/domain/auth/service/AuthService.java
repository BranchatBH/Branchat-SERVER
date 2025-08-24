package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.client.GoogleOAuthClient;
import com.b.h.Branchat.domain.auth.dto.response.AuthResults;
import com.b.h.Branchat.domain.auth.model.CheckMember;
import com.b.h.Branchat.domain.auth.client.dto.GoogleTokenResponse;
import com.b.h.Branchat.domain.auth.client.dto.GoogleUserInfo;
import com.b.h.Branchat.domain.auth.dto.response.NewTokensResponse;
import com.b.h.Branchat.domain.auth.model.TokenPair;
import com.b.h.Branchat.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final GoogleOAuthClient googleOAuthClient;
    private final JwtProvider jwtProvider;
    private final HashedRefreshTokenService hashedRefreshTokenService;
    private final AuthProviderService authProviderService;

    public AuthResults loginOrSignupWithCode(
        String code, String codeVerifier, String clientRedirectUri
    ){
        GoogleUserInfo googleUserInfo = getGoogleUserInfoFromGoogle(code, clientRedirectUri, codeVerifier); //구글에서 유저 정보 가져옴

        CheckMember checkMember = authProviderService.findOrCreateMember(googleUserInfo); //우리 회원인지 확인

        Member member = checkMember.member();
        TokenPair tokens = generateAndStoreTokens(member.getId());
        return new AuthResults(tokens.accessToken(), tokens.refreshToken(), checkMember.isNewMember());
    }

    public NewTokensResponse getNewTokens(String refreshToken) {
        // 1. refresh token으로부터 member id 추출
        Jws<Claims> claims = jwtProvider.parseClaims(refreshToken);
        UUID memberId = UUID.fromString(claims.getPayload().getSubject());

        // 2. redis의 refresh token과 일치하는지 확인
        hashedRefreshTokenService.validateRefreshToken(refreshToken, memberId);

        // 3. 기존 refresh token 삭제
        hashedRefreshTokenService.removeRefreshToken(refreshToken);

        TokenPair tokens = generateAndStoreTokens(memberId);
        return new NewTokensResponse(tokens.accessToken(), tokens.refreshToken());
    }

    private GoogleUserInfo getGoogleUserInfoFromGoogle(String code, String effectiveRedirectUri, String codeVerifier) {
        GoogleTokenResponse tokens = googleOAuthClient.getGoogleToken(code, effectiveRedirectUri, codeVerifier);
        return googleOAuthClient.getGoogleUserInfo(tokens.accessToken());
    }

    private TokenPair generateAndStoreTokens(UUID memberId) {
        String accessToken = jwtProvider.createAccessToken(memberId);
        String refreshToken = jwtProvider.createRefreshToken(memberId);
        hashedRefreshTokenService.saveRefreshTokenAsHash(refreshToken, memberId);
        return new TokenPair(accessToken, refreshToken);
    }
}
