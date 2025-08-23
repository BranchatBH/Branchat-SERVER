package com.b.h.Branchat.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfo(String sub, String name, String email,
                             @JsonProperty("picture") String profileImageUrl) {

}
