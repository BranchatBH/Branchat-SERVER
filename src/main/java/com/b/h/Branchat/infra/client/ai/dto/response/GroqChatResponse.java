package com.b.h.Branchat.infra.client.ai.dto.response;

import java.util.List;

// Simplified to only include the fields we need.
public record GroqChatResponse(
    List<GroqChoice> choices,
    GroqUsage usage
) {}
