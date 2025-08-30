package com.b.h.Branchat.infra.client.ai.dto.request;

public record GroqChatMessage(
    String role,
    String content
) {}
