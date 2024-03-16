package com.sm.project.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromMail;
    @Value("")
    private String url;

    public void sendResetPwdEmail(String email, String certificationCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);  //보내는 대상
        message.setSubject("[냉장고 해결사] 비밀번호 찾기 인증 메일입니다.");  //제목

        String content = "<div>"
                + "<p> 안녕하세요. 냉장고 해결사 입니다.<p>"
                + "<br>"
                + "<p> 아래 인증 코드를 앱에 입력하면 인증이 완료되고, " + email +" 계정의 비밀번호를 재설정 할 수 있습니다.<p>"
                + "<p> 새로운 비밀번호로 재설정 해주세요. <p>"
                + "<br>"
                + "<p> 인증코드: "+ certificationCode +"<P>"
                + "<br>"
                + "<p> 감사합니다. <p>"
                + "</div>";

        message.setFrom(new InternetAddress(fromMail, "냉장고 해결사")); //보내는 사람의 이메일, 이름
        message.setText(content, "utf-8", "html"); //내용, charset 타입, subtype
        mailSender.send(message); //메일 전송
    }
}
