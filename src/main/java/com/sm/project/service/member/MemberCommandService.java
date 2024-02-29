package com.sm.project.service.member;

import com.sm.project.domain.member.Member;
import com.sm.project.web.dto.member.MemberRequestDTO;

public interface MemberCommandService {

    public Member joinMember(MemberRequestDTO.JoinDTO request);

    public void sendSms(MemberRequestDTO.SmsDTO smsDTO);
}
