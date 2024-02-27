package com.sm.project.service.member;

import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.exception.handler.MemberHandler;
import com.sm.project.domain.member.Member;
import com.sm.project.repository.member.MemberRepository;
import com.sm.project.web.dto.member.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;

    public Member findEmail(String nickname, String phone) {
        return memberRepository.findByNicknameAndPhone(nickname, phone).orElseThrow(
                () -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
