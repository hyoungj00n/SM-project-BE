package com.sm.project.service.member;

import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.exception.handler.MemberHandler;
import com.sm.project.converter.member.MemberConverter;
import com.sm.project.coolsms.SmsUtil;
import com.sm.project.domain.member.Member;
import com.sm.project.domain.member.MemberPassword;
import com.sm.project.repository.member.MemberPasswordRepository;
import com.sm.project.repository.member.MemberRepository;
import com.sm.project.web.dto.member.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandServiceImpl implements MemberCommandService{

    private final MemberRepository memberRepository;
    private final MemberPasswordRepository memberPasswordRepository;
    private final PasswordEncoder encoder;
    private final SmsUtil smsUtil;

    @Transactional
    public Member joinMember(MemberRequestDTO.JoinDTO request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) { //이메일 중복 검사
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_JOIN);
        }
        Member newMember = MemberConverter.toMember(request);
        memberRepository.save(newMember);

        //패스워드 해시처리.
        String password = encoder.encode(request.getPassword());
        memberPasswordRepository.save(MemberConverter.toMemberPassword(password, newMember));
        return newMember;
    }

    public void sendSms(MemberRequestDTO.SmsDTO smsDTO) {
        String to = smsDTO.getPhone();
        int randomNum = (int) (Math.random()*9000)+1000;
        String vertificationCode = String.valueOf(randomNum);
        smsUtil.sendOne(to, vertificationCode); //인증문자 전송

        //redis에 저장하는 코드
    }
}
