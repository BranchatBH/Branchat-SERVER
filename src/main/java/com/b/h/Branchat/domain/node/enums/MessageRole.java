package com.b.h.Branchat.domain.node.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageRole {
    @JsonProperty("user") USER,
    @JsonProperty("ai") AI
}
