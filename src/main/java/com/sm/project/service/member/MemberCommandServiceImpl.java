package com.sm.project.service.member;

import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.exception.handler.MemberHandler;
import com.sm.project.converter.member.MemberConverter;
import com.sm.project.coolsms.RedisUtil;
import com.sm.project.coolsms.SmsUtil;
import com.sm.project.domain.member.Member;
import com.sm.project.service.mail.MailService;
import com.sm.project.repository.member.MemberPasswordRepository;
import com.sm.project.repository.member.MemberRepository;
import com.sm.project.web.dto.member.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandServiceImpl implements MemberCommandService{

    private final MemberRepository memberRepository;
    private final MemberPasswordRepository memberPasswordRepository;
    private final BCryptPasswordEncoder encoder;
    private final SmsUtil smsUtil;
    private final RedisUtil redisUtil;
    private final MemberQueryService memberQueryService;
    private final MailService mailService;

    @Transactional
    public Member joinMember(MemberRequestDTO.JoinDTO request) {
        verifySms(request.getPhone(), request.getCertificationCode()); //인증코드 검사

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
        redisUtil.createSmsCertification(to, vertificationCode);
    }

    @Override
    public void verifySms(String phone, String certificationCode) {
        if (isVerify(phone, certificationCode)) {
            throw new MemberHandler(ErrorStatus.MEMBER_VERIFY_FAILURE);
        }
        redisUtil.removeSmsCertification(phone);
    }

    @Override
    public boolean isVerify(String phone, String certificationCode) {
        return !(redisUtil.hasKey(phone) && redisUtil.getSmsCertification(phone).equals(certificationCode));
    }

    @Override
    @Transactional
    public void sendEmail(MemberRequestDTO.FindPasswordDTO request) throws MessagingException, UnsupportedEncodingException {
        Member member = memberQueryService.findByEmail(request.getEmail()); //가입된 메일인지 검사. null이면 에러발생

        String resetToken = UUID.randomUUID().toString();
        member.setResetToken(resetToken);
        mailService.sendResetPwdEmail(member.getEmail(), resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(String resetToken, MemberRequestDTO.PasswordDTO request) {
        Member member = memberQueryService.findByResetToken(resetToken);

        if (request.getNewPassword().equals(request.getPasswordCheck())) { //새비밀번호 일치한지 확인
            member.getMemberPassword().setPassword(encoder.encode(request.getNewPassword()));
            member.setResetToken(null); //재설정 토큰 다시 null로
        } else {
            throw new MemberHandler(ErrorStatus.MEMBER_PASSWORD_MISMATCH);
        }
    }
}
