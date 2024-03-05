package com.sm.project.repository.member;

import com.sm.project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByNicknameAndPhone(String nickname, String phone);

    Optional<Member> findByResetToken(String resetToken);
}

