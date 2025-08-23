package com.b.h.Branchat.domain.user.repository;

import com.b.h.Branchat.domain.user.entity.Member;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID>, MemberRepositoryCustom {

}
