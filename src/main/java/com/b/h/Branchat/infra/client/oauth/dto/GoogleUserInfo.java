package com.b.h.Branchat.infra.client.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfo(String sub, String name, String email,
                             @JsonProperty("picture") String profileImageUrl) {

}
