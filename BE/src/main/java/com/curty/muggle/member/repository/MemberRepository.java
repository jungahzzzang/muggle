package com.curty.muggle.member.repository;

import com.curty.muggle.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Member save(@NonNull Member member);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberId(Long memberId);
}
