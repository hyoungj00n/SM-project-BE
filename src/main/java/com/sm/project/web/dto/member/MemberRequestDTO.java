package com.sm.project.web.dto.member;

import lombok.Getter;
import lombok.Setter;

public class MemberRequestDTO {

    @Getter
    @Setter
    public static class IssueTokenDTO {
        String refreshToken;
    }
}
