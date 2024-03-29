package com.sm.project.feignClient.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KakaoProfile {
    public Long id;
    public String connected_at;
    public KakaoAccount kakao_account;

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    public class KakaoAccount {

        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        public Boolean has_phone_number;
        public Boolean phone_number_needs_agreement;
        public String phone_number;

    }
}
