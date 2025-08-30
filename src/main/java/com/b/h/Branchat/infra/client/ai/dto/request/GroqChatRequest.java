package com.b.h.Branchat.infra.client.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GroqChatRequest(
    List<GroqChatMessage> messages,
    String model,
    Double temperature,
    @JsonProperty("max_completion_tokens")
    Integer maxCompletionTokens,
    @JsonProperty("top_p")
    Double topP,
    Boolean stream,
    String stop
) {}
