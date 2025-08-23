package com.b.h.Branchat.domain.auth.dto.response;

public record LoginResponse(
    boolean exists,
    String accessToken,
    String refreshToken,
    String message
) {

}

