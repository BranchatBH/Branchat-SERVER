package com.b.h.Branchat.infra.client.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

// Simplified to only include total_tokens and total_time.
public record GroqUsage(
    @JsonProperty("total_tokens")
    int totalTokens,
    @JsonProperty("total_time")
    double totalTime
) {}
