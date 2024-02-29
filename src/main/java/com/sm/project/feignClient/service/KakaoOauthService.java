package com.sm.project.feignClient.service;

import com.sm.project.feignClient.dto.KakaoProfile;
import com.sm.project.feignClient.kakao.KakaoInfoFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KakaoOauthService  {

    private final KakaoInfoFeignClient kakaoInfoFeignClient;
    public KakaoProfile getKakaoUserInfo(String token) {
        KakaoProfile kakaoProfile = kakaoInfoFeignClient.getInfo(token);

        return kakaoProfile;
    }
}
