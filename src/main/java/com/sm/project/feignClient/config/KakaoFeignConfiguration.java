package com.sm.project.feignClient.config;

import com.sm.project.feignClient.FeignClientException;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class KakaoFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        };
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientException();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
