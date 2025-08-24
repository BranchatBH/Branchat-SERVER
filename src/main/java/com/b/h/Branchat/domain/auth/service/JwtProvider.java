package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${spring.jwt.secret}")
    private String secret;
    private SecretKey secretKey;
    @Value("${spring.jwt.access-token-expiration-milliseconds}")
    long accessTokenExpirationMilliseconds;
    @Value("${spring.jwt.refresh-token-expiration-milliseconds}")
    long refreshTokenExpirationMilliseconds;

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        keyBytes = Base64.getDecoder().decode(secret.trim());
        validateKeyLength(keyBytes);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes); //byte로 구성된 keybytes를 secret key 객체로 바꿈
    }

    public Jws<Claims> parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> jws = parseClaims(token);
        Header header = jws.getHeader();
        Claims claims = jws.getPayload();
        String userId = claims.getSubject();

        validateIsUser(userId);

        return new UsernamePasswordAuthenticationToken(
            userId,
            null,
            Collections.emptyList());
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getPayload().getExpiration();
    }

    public String createAccessToken(UUID memberId) {
        return createTokenInternal(memberId, accessTokenExpirationMilliseconds, "access");
    }

    public String createRefreshToken(UUID memberId) {
        return createTokenInternal(memberId, refreshTokenExpirationMilliseconds, "refresh");
    }

    private void validateIsUser(String userId) {
        if (!memberRepository.existsById(UUID.fromString(userId))) {
            throw new UsernameNotFoundException("User not found: " + userId);
        }
    }

    private String createTokenInternal(UUID memberId, long expirationMillis, String type) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
            .subject(String.valueOf(memberId))
            .issuedAt(now)
            .expiration(expiry)
            .claim("typ", type) // access/refresh 구분 클레임 추가하면 검증 시 유용
            .signWith(secretKey) // jjwt 0.12.x 스타일
            .compact();
    }

    private void validateKeyLength(byte[] keyBytes) {
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                "JWT secret key must be at least 32 bytes for HS256 (current: "
                    + keyBytes.length + " bytes)"
            );
        }
    }
}
