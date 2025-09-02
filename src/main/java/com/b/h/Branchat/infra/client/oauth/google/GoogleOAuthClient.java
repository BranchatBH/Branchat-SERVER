package com.b.h.Branchat.infra.client.oauth.google;

import static com.b.h.Branchat.domain.auth.exception.AuthErrorCode.GOOGLE_TOKEN_RETRIEVAL_FAILED;
import static com.b.h.Branchat.domain.auth.exception.AuthErrorCode.GOOGLE_USER_INFO_RETRIEVAL_FAILED;
import static com.b.h.Branchat.domain.auth.exception.AuthErrorCode.UNAUTHORIZED;

import com.b.h.Branchat.domain.auth.exception.AuthException;
import com.b.h.Branchat.infra.client.oauth.google.config.GoogleOauthProperties;
import com.b.h.Branchat.infra.client.oauth.google.dto.GoogleTokenResponse;
import com.b.h.Branchat.infra.client.oauth.google.dto.GoogleUserInfo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class GoogleOAuthClient {

    private final RestClient authRestClient;
    private final GoogleOauthProperties properties;

    public GoogleOAuthClient(@Qualifier("authRestClient") RestClient authRestClient, GoogleOauthProperties properties) {
        this.authRestClient = authRestClient;
        this.properties = properties;
    }

    public GoogleTokenResponse getGoogleToken(String code, String redirectUri, String codeVerifier) {
        log.info("getGoogleToken 호출됨");
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", properties.clientId());
        form.add("client_secret", properties.clientSecret());
        form.add("code_verifier", codeVerifier);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", properties.grantType());
        try {
            GoogleTokenResponse response = authRestClient.post()
                .uri(properties.tokenUri())
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

    public GoogleUserInfo getGoogleUserInfo(String accessToken) {
        return authRestClient.get()
            .uri(properties.userInfoUri())
            .headers(h -> h.setBearerAuth(accessToken))
            .retrieve()
            //파라미터 누락, 존재하지 않는 사용자, 비활성 사용자(구글에서) 등 처리
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                throw new AuthException(
                    UNAUTHORIZED,
                    "Google userinfo 4xx error: " + readErrorBody(res)
                );
            })
            //구글 터짐
            .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                throw new AuthException(
                    GOOGLE_USER_INFO_RETRIEVAL_FAILED,
                    "Google userinfo 5xx error: " + readErrorBody(res)
                );
            })
            .body(GoogleUserInfo.class);
    }

    private String readErrorBody(ClientHttpResponse res) {
        try (var is = res.getBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "(error reading body: " + e.getMessage() + ")";
        }
    }

}
