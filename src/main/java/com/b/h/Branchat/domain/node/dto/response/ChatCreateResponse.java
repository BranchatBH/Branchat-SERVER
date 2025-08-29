package com.b.h.Branchat.domain.node.dto.response;

import java.util.UUID;

public record ChatCreateResponse(UUID id,
                                 UUID parentId,
                                 String sourceChatId,
                                 String title,
                                 String parentContext
) {

}
