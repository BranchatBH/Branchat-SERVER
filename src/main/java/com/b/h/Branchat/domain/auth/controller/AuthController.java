package com.b.h.Branchat.domain.auth.controller;

import static com.b.h.Branchat.domain.auth.controller.AuthMessage.LOGIN_SIGNUP_SUCCESS;
import static com.b.h.Branchat.domain.auth.controller.AuthMessage.LOGIN_SUCCESS;
import static com.b.h.Branchat.domain.auth.controller.AuthMessage.NEW_TOKENS_ISSUED;
import static com.b.h.Branchat.domain.auth.controller.AuthMessage.REFRESH_TOKEN_DELETED;
import static com.b.h.Branchat.domain.auth.controller.AuthMessage.SIGNUP_SUCCESS;

import com.b.h.Branchat.domain.auth.dto.request.GoogleAuthCodeRequest;
import com.b.h.Branchat.domain.auth.dto.request.NewTokensRequest;
import com.b.h.Branchat.domain.auth.dto.response.AuthResults;
import com.b.h.Branchat.domain.auth.dto.response.LoginResponse;
import com.b.h.Branchat.domain.auth.dto.response.NewTokensResponse;
import com.b.h.Branchat.domain.auth.service.AuthService;
import com.b.h.Branchat.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login/google")
    public ResponseEntity<ApiResponse<LoginResponse, Void>> googleCallback(
        @Valid @RequestBody GoogleAuthCodeRequest request,
        @RequestParam(value = "redirect_uri", required = false) String clientRedirectUri) {

        AuthResults authResults = authService.loginOrSignupWithCode(
            request.code(),
            request.codeVerifier(),
            clientRedirectUri);

        //회원가입 응답
        if (authResults.isNewUser()) {
            LoginResponse loginResponse = new LoginResponse(false, authResults.accessToken(),
                authResults.refreshToken(),
                SIGNUP_SUCCESS);
            log.info(loginResponse.toString());
            return ResponseEntity.ok(ApiResponse.ok(LOGIN_SIGNUP_SUCCESS, loginResponse));
        }

        //로그인 응답
        LoginResponse loginResponse = new LoginResponse(true, authResults.accessToken(),
            authResults.refreshToken(), LOGIN_SUCCESS);
        log.info(loginResponse.toString());
        return ResponseEntity.ok(ApiResponse.ok(LOGIN_SIGNUP_SUCCESS, loginResponse));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<NewTokensResponse, Void>> refreshCallback(@Valid @RequestBody
        NewTokensRequest request){
        NewTokensResponse response = authService.getNewTokens(request.refreshToken());
        log.info(response.toString());
        return ResponseEntity.ok(ApiResponse.ok(NEW_TOKENS_ISSUED, response));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<Void, Void>> logout(
            Authentication authentication,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        
        String accessToken = authorizationHeader.substring(7);
        UUID memberId = UUID.fromString(authentication.getName());

        authService.invalidateTokens(memberId, accessToken);
        
        return ResponseEntity.ok(ApiResponse.ok(REFRESH_TOKEN_DELETED));
    }
}
