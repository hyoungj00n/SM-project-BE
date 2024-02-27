package com.sm.project.feignClient.kakao;

import com.sm.project.feignClient.config.KakaoFeignConfiguration;
import com.sm.project.feignClient.dto.KakaoProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "KakaoFeignClient", url = "${oauth2.kakao.info-url}", configuration = KakaoFeignConfiguration.class)
public interface KakaoInfoFeignClient {

    @GetMapping("/v2/user/me")
    KakaoProfile getInfo(@RequestHeader(name = "Authorization") String Authorization);
}
