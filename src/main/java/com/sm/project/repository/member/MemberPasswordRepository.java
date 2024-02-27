package com.sm.project.repository.member;

import com.sm.project.domain.member.MemberPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPasswordRepository extends JpaRepository<MemberPassword, Long> {

}
