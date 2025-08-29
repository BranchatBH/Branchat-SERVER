package com.b.h.Branchat.domain.node.dto.request;

import com.b.h.Branchat.domain.node.enums.MessageRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageContent(
    @NotNull(message = "메시지 역할은 필수입니다.")
    MessageRole role,

    @NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
    String content
) {}
