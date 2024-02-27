package com.sm.project.web.dto.member;

import lombok.*;

public class MemberRequestDTO {

    @Getter
    @Setter
    public static class IssueTokenDTO {
        String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        private String email;
        private String password;
    }


}
