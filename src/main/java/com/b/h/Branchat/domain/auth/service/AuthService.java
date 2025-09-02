package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.infra.client.oauth.google.GoogleOAuthClient;
import com.b.h.Branchat.domain.auth.dto.response.AuthResults;
import com.b.h.Branchat.domain.auth.model.CheckMember;
import com.b.h.Branchat.infra.client.oauth.google.dto.GoogleTokenResponse;
import com.b.h.Branchat.infra.client.oauth.google.dto.GoogleUserInfo;
import com.b.h.Branchat.domain.auth.dto.response.NewTokensResponse;
import com.b.h.Branchat.domain.auth.model.TokenPair;
import com.b.h.Branchat.domain.auth.repository.HashedRefreshTokenRepository;
import com.b.h.Branchat.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final GoogleOAuthClient googleOAuthClient;
    private final JwtProvider jwtProvider;
    private final HashedRefreshTokenService hashedRefreshTokenService;
    private final AuthProviderService authProviderService;
    private final HashedRefreshTokenRepository hashedRefreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenHasher tokenHasher;

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

    public void invalidateTokens(UUID memberId, String accessToken) {
        // 1. 기존 Refresh Token 삭제
        hashedRefreshTokenRepository.deleteByMemberId(memberId.toString());

        // 2. Access Token을 블랙리스트로 추가
        blacklistAccessToken(accessToken);
    }

    public void blacklistAccessToken(String accessToken) {
        // Access Token을 해시하여 Redis에 블랙리스트로 추가
        String hashedAccessToken = tokenHasher.hash(accessToken);
        Date expiration = jwtProvider.getExpiration(accessToken);
        long now = new Date().getTime();
        long remainingTime = expiration.getTime() - now;

        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(hashedAccessToken, "logout", remainingTime, TimeUnit.MILLISECONDS);
            log.info("토큰 블랙리스트 추가 성공. Hashed Token: {}, 만료 시간: {}ms", hashedAccessToken, remainingTime);
        }
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
