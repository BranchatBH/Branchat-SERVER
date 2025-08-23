package com.b.h.Branchat.domain.auth.controller;

import static com.b.h.Branchat.domain.auth.message.AuthMessage.LOGIN_SIGNUP_SUCCESS;
import static com.b.h.Branchat.domain.auth.message.AuthMessage.LOGIN_SUCCESS;
import static com.b.h.Branchat.domain.auth.message.AuthMessage.SIGNUP_SUCCESS;

import com.b.h.Branchat.domain.auth.dto.request.GoogleAuthCodeRequest;
import com.b.h.Branchat.domain.auth.dto.response.AuthResults;
import com.b.h.Branchat.domain.auth.dto.response.LoginResponse;
import com.b.h.Branchat.domain.auth.service.AuthService;
import com.b.h.Branchat.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            return ResponseEntity.ok(ApiResponse.ok(LOGIN_SIGNUP_SUCCESS, loginResponse));
        }

        //로그인 응답
        LoginResponse loginResponse = new LoginResponse(true, authResults.accessToken(),
            authResults.refreshToken(), LOGIN_SUCCESS);
        log.info(loginResponse.toString());
        return ResponseEntity.ok(ApiResponse.ok(LOGIN_SIGNUP_SUCCESS, loginResponse));
    }
}
