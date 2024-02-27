package com.sm.project.service.member;

import com.sm.project.domain.member.Member;
import com.sm.project.web.dto.member.MemberRequestDTO;

public interface MemberQueryService {
    public Member findEmail(String nickname, String phone);
}
