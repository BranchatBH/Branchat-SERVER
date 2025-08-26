package com.b.h.Branchat.domain.node.repository;

import com.b.h.Branchat.domain.member.entity.Member;
import com.b.h.Branchat.domain.node.entity.Node;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NodeRepository extends JpaRepository<Node, UUID> {

    @Modifying
    @Query("delete from Node n where n.member = :member")
    void deleteAllByMember(@Param("member") Member member);
}
