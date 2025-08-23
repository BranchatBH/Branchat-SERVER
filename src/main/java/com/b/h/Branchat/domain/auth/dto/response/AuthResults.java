package com.b.h.Branchat.domain.auth.dto.response;

public record AuthResults(
    String accessToken,
    String refreshToken,
    boolean isNewUser
) {}
