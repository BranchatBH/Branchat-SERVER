package com.b.h.Branchat.domain.auth.service;

import com.b.h.Branchat.domain.auth.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public LoginResponse loginOrSignupWithCode(
        String code, String codeVerifier, String client_redirect_uri, HttpServletResponse response
    ){

    }

}
