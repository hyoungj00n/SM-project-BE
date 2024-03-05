package com.sm.project.service.member;

import com.sm.project.domain.member.Member;
import com.sm.project.web.dto.member.MemberRequestDTO;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MemberCommandService {

    public Member joinMember(MemberRequestDTO.JoinDTO request);
    public boolean isDuplicate(MemberRequestDTO.NicknameDTO request);

    public void sendSms(MemberRequestDTO.SmsDTO smsDTO);
    public void verifySms(String phone, String certificationCode);

    public boolean isVerify(String phone, String certificationCode);
    public void sendEmail(MemberRequestDTO.FindPasswordDTO request) throws MessagingException, UnsupportedEncodingException;

    public void resetPassword(String resetToken, MemberRequestDTO.PasswordDTO request);
}
