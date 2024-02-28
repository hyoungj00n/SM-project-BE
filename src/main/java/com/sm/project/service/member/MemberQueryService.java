package com.sm.project.service.member;

import com.sm.project.domain.member.Member;
import com.sm.project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;
    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }
}
