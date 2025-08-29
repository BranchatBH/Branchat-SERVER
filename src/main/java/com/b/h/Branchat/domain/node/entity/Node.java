package com.b.h.Branchat.domain.node.entity;

import com.b.h.Branchat.domain.member.entity.Member;
import com.b.h.Branchat.domain.node.enums.AiModel;
import com.b.h.Branchat.domain.node.enums.NodeType;
import com.b.h.Branchat.global.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Table(name = "node")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Node extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeType type;

    @Column(nullable = false)
    private String title;

    private String sourceChatId;

    @Enumerated(EnumType.STRING)
    private AiModel sourceAiModel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String branchSourceInfo; // JSONB 타입, 보통 String이나 Map<String, Object>로 매핑

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String contextCache;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_node_id")
    @JsonBackReference //여기서 막힘. 이 node는 Json 직렬화 안됨
    private Node parentNode;

    @OneToMany(mappedBy = "parentNode")
    @JsonManagedReference // 얘는 json 직렬화 됨. 즉, 자식 보여줌.
    private List<Node> children = new ArrayList<>();

    public void setParentNode(Node parentNode) {
        if (this.parentNode != null) {
            this.parentNode.getChildren().remove(this);
        }
        this.parentNode = parentNode;
        if (parentNode != null) {
            parentNode.getChildren().add(this);
        }
    }

    public static Node create(Member member,
                              NodeType type,
                              String title,
                              String sourceChatId,
                              AiModel sourceAiModel,
                              String branchSourceInfo,
                              String contextCache,
                              Node parentNode) {
        Node node = Node.builder()
                .member(member)
                .type(type)
                .title(title)
                .sourceChatId(sourceChatId)
                .sourceAiModel(sourceAiModel)
                .branchSourceInfo(branchSourceInfo)
                .contextCache(contextCache)
                .build();
        node.setParentNode(parentNode);
        return node;
    }

}
