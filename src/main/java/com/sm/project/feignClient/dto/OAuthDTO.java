package com.sm.project.feignClient.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthDTO {
    private String email;
    private String profileUrl;
}
