package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.client.GoogleOAuthClient;
import com.b.h.Branchat.domain.auth.dto.response.GoogleTokenResponse;
import com.b.h.Branchat.domain.auth.dto.response.GoogleUserInfo;
import com.b.h.Branchat.domain.auth.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
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
    @Value("${GOOGLE_CLIENT_ID}")
    String clientId;
    @Value("${GOOGLE_REDIRECT_URI}")
    String defaultRedirectUri;
    public LoginResponse loginOrSignupWithCode(
        String code, String codeVerifier, String clientRedirectUri, HttpServletResponse response
    ){
        String effectiveRedirectUri = resolveRedirectUri(clientRedirectUri);
        GoogleTokenResponse tokens = googleOAuthClient.getGoogleToken(code, effectiveRedirectUri, codeVerifier);
        GoogleUserInfo googleUserInfo = googleOAuthClient.getGoogleUserInfo(
            tokens.accessToken());
    }

    private String resolveRedirectUri(String clientRedirectUri) {
        if (clientRedirectUri != null && ALLOWED_REDIRECT_URIS.contains(clientRedirectUri)) {
            return clientRedirectUri;
        }
        return this.defaultRedirectUri;   // 기본값(배포용) 사용
    }
}
