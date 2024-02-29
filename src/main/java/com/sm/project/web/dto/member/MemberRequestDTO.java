package com.sm.project.web.dto.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class MemberRequestDTO {

    @Getter
    @Setter
    public static class IssueTokenDTO {
        String refreshToken;
    }

    @Getter
    public static class JoinDTO {
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문, 숫자, 특수문자를 사용하세요.")
        String password;

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        String nickname;

        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        String phone;

        Boolean infoAgree;
        Boolean messageAgree;
    }

    @Getter
    public static class SmsDTO {
        private String phone;
    }
}
