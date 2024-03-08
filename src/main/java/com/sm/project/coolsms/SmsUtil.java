package com.sm.project.coolsms;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.senderNumber}")
    private String senderNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    //메세지 발송
    public SingleMessageSentResponse sendOne(String to, String vertificationCode) {
        Message message = new Message();
        //전화번호는 반드시 01012341234 형태로 입력
        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText("[냉장고 해결사] 인증번호를 입력해주세요.\n" + vertificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }
}
