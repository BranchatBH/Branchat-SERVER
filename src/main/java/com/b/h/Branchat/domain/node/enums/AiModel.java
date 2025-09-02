package com.b.h.Branchat.domain.node.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AiModel {
    @JsonProperty("chatgpt") CHATGPT,
    @JsonProperty("gemini") GEMINI,
    @JsonProperty("claude") CLAUDE,
    @JsonProperty("grok") GROK,
    @JsonProperty("perplexity") PERPLEXITY
}
