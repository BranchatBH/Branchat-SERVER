package com.b.h.Branchat.domain.auth.dto.response;

public record NewTokensResponse(
    String accessToken,
    String refreshToken) {

}
