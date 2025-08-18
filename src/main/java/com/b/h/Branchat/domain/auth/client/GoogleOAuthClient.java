package com.b.h.Branchat.domain.auth.client;

import static com.b.h.Branchat.domain.auth.exception.AuthErrorCode.GOOGLE_TOKEN_RETRIEVAL_FAILED;

import com.b.h.Branchat.domain.auth.dto.response.GoogleTokenResponse;
import com.b.h.Branchat.domain.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuthClient {
    private final RestClient authRestClient;
    @Value("${GOOGLE_CLIENT_ID}")
    String clientId;

    public GoogleTokenResponse getGoogleToken(String code, String redirectUri, String codeVerifier) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("code_verifier", codeVerifier);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");
        try {
            GoogleTokenResponse response = authRestClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(GoogleTokenResponse.class);
            log.info("Google 응답 수신: {}", response);
            if (response == null) {
                log.warn("Google 응답이 null입니다.");
                throw new AuthException(GOOGLE_TOKEN_RETRIEVAL_FAILED);
            }
            if (response.accessToken() == null) {
                log.warn("Google 응답에서 accessToken이 null입니다: {}", response);
                throw new AuthException(GOOGLE_TOKEN_RETRIEVAL_FAILED);
            }
            return response;
        } catch (Exception e) {
            log.error("Google 토큰 요청 실패", e);
            throw new AuthException(GOOGLE_TOKEN_RETRIEVAL_FAILED, e.getMessage());
        }
    }
}
