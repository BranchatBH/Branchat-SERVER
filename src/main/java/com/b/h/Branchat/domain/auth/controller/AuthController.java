package com.b.h.Branchat.domain.auth.controller;

import static com.b.h.Branchat.domain.auth.message.AuthMessage.LOGIN_SIGNUP_SUCCESS;
import static com.b.h.Branchat.domain.auth.message.AuthMessage.LOGIN_SUCCESS;
import static com.b.h.Branchat.domain.auth.message.AuthMessage.SIGNUP_SUCCESS;

import com.b.h.Branchat.domain.auth.dto.request.GoogleAuthCodeRequest;
import com.b.h.Branchat.domain.auth.dto.response.AuthResults;
import com.b.h.Branchat.domain.auth.dto.response.LoginResponse;
import com.b.h.Branchat.domain.auth.service.AuthService;
import com.b.h.Branchat.domain.auth.service.RefreshTokenService;
import com.b.h.Branchat.global.dto.response.ApiResponse;
import com.b.h.Branchat.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/auth/oauth2/google/callback")
    public ResponseEntity<ApiResponse<LoginResponse, Void>> googleCallback(
        @Valid @RequestBody GoogleAuthCodeRequest request,
        @RequestParam(value = "redirect_uri", required = false) String clientRedirectUri,
        HttpServletResponse response) {

        AuthResults authResults = authService.loginOrSignupWithCode(
            request.code(),
            request.codeVerifier(),
            clientRedirectUri);

        //쿠키에 리프레시 토큰 담기
        cookieUtil.createRefreshTokenCookie(response, authResults.refreshToken());

        //회원가입 응답
        if (authResults.isNewUser()) {
            LoginResponse loginResponse = new LoginResponse(false, authResults.accessToken(),
                SIGNUP_SUCCESS);
            return ResponseEntity.ok(ApiResponse.ok(LOGIN_SIGNUP_SUCCESS, loginResponse));
        }

        //로그인 응답
        LoginResponse loginResponse = new LoginResponse(true, authResults.accessToken(), LOGIN_SUCCESS);
        return ResponseEntity.ok(ApiResponse.ok(LOGIN_SIGNUP_SUCCESS, loginResponse));
    }
}
