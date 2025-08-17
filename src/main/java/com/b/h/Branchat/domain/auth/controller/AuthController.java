package com.b.h.Branchat.domain.auth.controller;

import static com.b.h.Branchat.domain.auth.message.AuthMessage.LOGIN_SIGNUP_SUCCESS;

import com.b.h.Branchat.domain.auth.dto.request.GoogleAuthCodeRequest;
import com.b.h.Branchat.domain.auth.dto.response.LoginResponse;
import com.b.h.Branchat.domain.auth.service.AuthService;
import com.b.h.Branchat.global.dto.response.ApiResponse;
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

    @PostMapping("/auth/oauth2/google/callback")
    public ResponseEntity<ApiResponse<LoginResponse, Void>> googleCallback(
        @Valid @RequestBody GoogleAuthCodeRequest request,
        @RequestParam(value = "redirect_uri", required = false) String clientRedirectUri,
        HttpServletResponse response) {
        LoginResponse giveback = authService.loginOrSignupWithCode(
            request.code(),
            request.codeVerifier(),
            clientRedirectUri,
            response);
        return ResponseEntity.ok(ApiResponse.ok(LOGIN_SIGNUP_SUCCESS, giveback));
    }
}
