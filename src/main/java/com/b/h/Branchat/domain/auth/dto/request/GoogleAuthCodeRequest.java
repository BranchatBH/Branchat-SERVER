package com.b.h.Branchat.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record GoogleAuthCodeRequest(
    @NotBlank(message = "인증 코드를 입력해주세요.")
    String code,
    @JsonProperty("code_verifier")
    String codeVerifier
) {

}
