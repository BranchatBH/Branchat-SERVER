package com.b.h.Branchat.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record GoogleAuthCodeRequest(
    @NotBlank(message = "인증 코드를 입력해주세요.")
    String code,
    @NotBlank(message = "code_verifier를 입력해주세요.")
    @Size(min = 43, max = 128, message = "code_verifier 길이는 43~128자여야 합니다.")
    @Pattern(regexp = "^[A-Za-z0-9\\-\\._~]+$", message = "code_verifier 형식이 올바르지 않습니다.")
    @JsonProperty("code_verifier")
    String codeVerifier
) {

}
