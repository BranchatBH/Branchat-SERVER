package com.b.h.Branchat.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewTokensRequest(
    @NotBlank(message = "Refresh token must not be blank")
    @Size(min = 20, max = 500, message = "Refresh token length must be between 20 and 500 characters")
    String refreshToken
) {

}
