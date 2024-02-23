package com.sm.project.web.dto.member;

import lombok.*;

public class MemberResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginDTO {
        private String accessToken;
        private String refreshToken;
    }
}
