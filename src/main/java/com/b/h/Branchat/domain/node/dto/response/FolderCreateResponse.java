package com.b.h.Branchat.domain.node.dto.response;

import java.util.UUID;

public record FolderCreateResponse(UUID id, UUID parentId, String title) {

}
