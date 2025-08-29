package com.b.h.Branchat.domain.node.dto.request;

import com.b.h.Branchat.domain.node.enums.AiModel;
import com.b.h.Branchat.domain.node.enums.NodeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record NodeCreateRequest(String parentId,

                                @NotNull(message = "type은 필수입니다.")
                                NodeType type,

                                @NotBlank(message = "title은 비어 있을 수 없습니다.")
                                @Size(max = 200, message = "title은 최대 200자입니다.")
                                String title,

                                String sourceChatId,

                                AiModel sourceAiModel,

                                @NotEmpty(message = "메시지 목록은 비어 있을 수 없습니다.")
                                List<@Valid MessageContent> messages,

                                @Valid
                                BranchSourceInfo branchSourceInfo) {

}
