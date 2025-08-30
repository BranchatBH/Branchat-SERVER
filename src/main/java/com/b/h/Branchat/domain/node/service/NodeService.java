package com.b.h.Branchat.domain.node.service;

import static com.b.h.Branchat.domain.node.exception.MemberErrorCode.USER_NOT_FOUND;
import static com.b.h.Branchat.domain.node.exception.NodeErrorCode.PARENT_NODE_NOT_FOUND;

import com.b.h.Branchat.domain.member.entity.Member;
import com.b.h.Branchat.domain.member.repository.MemberRepository;
import com.b.h.Branchat.domain.node.dto.request.ChatCreateRequest;
import com.b.h.Branchat.domain.node.dto.response.ChatCreateResponse;
import com.b.h.Branchat.domain.node.entity.Node;
import com.b.h.Branchat.domain.node.enums.NodeType;
import com.b.h.Branchat.domain.node.exception.MemberException;
import com.b.h.Branchat.domain.node.exception.NodeException;
import com.b.h.Branchat.domain.node.repository.NodeRepository;
import com.b.h.Branchat.domain.summarize.service.SummarizeService;
import com.b.h.Branchat.global.util.JsonConverter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NodeService {

  private final MemberRepository memberRepository;
  private final NodeRepository nodeRepository;
  private final SummarizeService summarizeService;
  private final JsonConverter jsonConverter;

  @Transactional
  public ChatCreateResponse createChat(ChatCreateRequest request, UUID memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new MemberException(USER_NOT_FOUND));
    Node parentNode = nodeRepository.findById(UUID.fromString(request.parentId()))
        .orElseThrow(() -> new NodeException(PARENT_NODE_NOT_FOUND));

    //if parentNode.type==folder -> parentContext = grandParentContext
    //if free tier && parentNode.context_cache exists -> parentContext = parentNode.context_cache
//    String parentContext = summarizeService.freeTierSummarize(request.messages());
    String parentContext = null;

    //if member is free tier
    Node contextProviderNode = findNearestChatAncestor(parentNode);

    if(contextProviderNode != null) { // 최상위 조상이 folder가 아니었다.(상위 chat이 존재한다.)
      if(contextProviderNode.getContextCache() == null) { // 상위 chat이 context가 아직 없음 -> message로 만들어. (이 경우 직계 상위 chat임. branch로 폴더나 채팅이 파이면 무조건 context가 존재하기 때문// folder 생성 로직에 부모 context 만드는 로직 추가하기)
        parentContext = summarizeService.freeTierSummarize(request.messages());
      } else { // context가 존재하는 경우 -> 그냥 가져다 쓰면 됨
        parentContext = contextProviderNode.getContextCache();
      }
    } // 최상위 조상이 folder였다. -> 내가 최상위 chat임. -> parentContext = null 유지.



    //

    //if member is pro tier

    //

    String branchSourceInfoJson = jsonConverter.toJson(request.branchSourceInfo());

    Node newChat = Node.create(member, NodeType.CHAT, request.title(), request.sourceChatId(),
        request.sourceAiModel(), branchSourceInfoJson, null, parentNode);
    Node savedNode = nodeRepository.save(newChat);

    UUID parentId = (savedNode.getParentNode() != null) ? savedNode.getParentNode().getId() : null;
    return new ChatCreateResponse(savedNode.getId(), parentId, savedNode.getSourceChatId(), savedNode.getTitle(), parentContext);
  }

  private Node findNearestChatAncestor(Node startNode) {
    Node currentNode = startNode;

    // 현재 노드가 FOLDER이고, 부모가 존재하는 동안 계속 루프를 돕니다.
    while (currentNode.getType() == NodeType.FOLDER && currentNode.getParentNode() != null) {
      currentNode = currentNode.getParentNode();
    }

    // 루프가 끝난 후, 최종 currentNode가 CHAT 타입이면 반환하고,
    // FOLDER 타입이면 (최상위 루트 폴더인 경우) null을 반환합니다.
    return (currentNode.getType() == NodeType.CHAT) ? currentNode : null;
  }
}
