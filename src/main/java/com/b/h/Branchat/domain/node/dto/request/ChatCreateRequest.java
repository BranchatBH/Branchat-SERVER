package com.b.h.Branchat.domain.node.dto.request;

import com.b.h.Branchat.domain.node.enums.AiModel;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ChatCreateRequest(
    @NotBlank(message = "parentId는 필수입니다.")
    @Pattern(
        regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
        message = "parentId 형식(UUID)이 올바르지 않습니다."
    ) String parentId,

    @NotBlank(message = "title은 비어 있을 수 없습니다.")
    @Size(max = 200, message = "title은 최대 200자입니다.")
    String title,

    String sourceChatId,

    AiModel sourceAiModel,

    @NotEmpty(message = "메시지 목록은 비어 있을 수 없습니다.")
    List<@Valid MessageContent> messages,

    JsonNode branchSourceInfo) {

    @AssertTrue(message = "부모 채팅이 있으면 sourceChatId, branchSourceInfo가 둘다 채워지고, 없으면 둘 다 빈칸")
    public boolean isBranchConsistencyValid() {
        return (sourceChatId == null && branchSourceInfo == null)
            || (sourceChatId != null && branchSourceInfo != null);
    }

}
