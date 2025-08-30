package com.b.h.Branchat.domain.node.controller;

import static com.b.h.Branchat.domain.node.controller.message.NodeMessage.NODE_CREATED;

import com.b.h.Branchat.domain.node.dto.request.ChatCreateRequest;
import com.b.h.Branchat.domain.node.dto.response.ChatCreateResponse;
import com.b.h.Branchat.domain.node.service.NodeService;
import com.b.h.Branchat.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class NodeController {
    private final NodeService nodeService;

    @PostMapping("/nodes/chat")
    public ResponseEntity<ApiResponse<ChatCreateResponse, Void>> createChat(@Valid @RequestBody ChatCreateRequest request, Authentication authentication) {
        UUID memberId = UUID.fromString(authentication.getName());
        ChatCreateResponse response = nodeService.createChat(request, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(NODE_CREATED,response));
    }
}
