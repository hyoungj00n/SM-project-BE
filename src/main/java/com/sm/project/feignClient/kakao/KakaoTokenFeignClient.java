package com.sm.project.feignClient.kakao;

import com.sm.project.feignClient.config.KakaoFeignConfiguration;
import com.sm.project.feignClient.dto.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "KakaoTokenFeignClient", url = "${oauth2.kakao.base-url}", configuration = KakaoFeignConfiguration.class)
public interface KakaoTokenFeignClient {

    @PostMapping(value = "/oauth/token")
    KakaoTokenResponse generateToken(@RequestParam(value = "grant_type") String grantType,
                                     @RequestParam(value = "client_id") String clientId,
                                     @RequestParam(value = "redirect_uri") String redirectUri,
                                     @RequestParam(value = "code") String code);

}
