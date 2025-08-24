package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.exception.AuthErrorCode;
import com.b.h.Branchat.domain.auth.exception.AuthException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class TokenHasher {

    public String hash(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // 이 예외는 SHA-256 알고리즘이 존재하지 않을 때 발생하지만,
            // 표준 Java 환경에서는 사실상 발생하지 않습니다.
            throw new AuthException(AuthErrorCode.INTERNAL_SERVER_ERROR, "토큰 해싱 중 서버 오류가 발생했습니다.");
        }
    }
}
