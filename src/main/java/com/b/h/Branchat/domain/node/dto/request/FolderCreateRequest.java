package com.b.h.Branchat.domain.node.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record FolderCreateRequest(UUID parentId,

                                  @NotBlank(message = "title은 비어 있을 수 없습니다.")
                                  @Size(max = 200, message = "title은 최대 200자입니다.")
                                  String title) {

}
