package com.b.h.Branchat.domain.node.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;

public record ChatCreateResponse(UUID id,
                                 UUID parentId,
                                 String sourceChatId,
                                 String title,
                                 JsonNode context
) {

}
