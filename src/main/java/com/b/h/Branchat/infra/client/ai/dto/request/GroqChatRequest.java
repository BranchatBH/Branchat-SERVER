package com.b.h.Branchat.infra.client.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GroqChatRequest(
    List<GroqChatMessage> messages,
    String model,
    double temperature,
    @JsonProperty("max_completion_tokens")
    int maxCompletionTokens,
    @JsonProperty("top_p")
    double topP,
    boolean stream,
    String stop
) {}
