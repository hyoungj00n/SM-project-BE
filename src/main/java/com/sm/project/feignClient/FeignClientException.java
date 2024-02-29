package com.sm.project.feignClient;

import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.exception.handler.FeignClientHandler;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeignClientException implements ErrorDecoder {

    Logger logger = LoggerFactory.getLogger(FeignClientException.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            logger.error("{}번 에러 발생 : {}", response.status(), response.reason());
            return new FeignClientHandler(ErrorStatus.FEIGN_CLIENT_ERROR);
        } else {
            logger.error("500번대 에러 발생 : {}", response.reason());
            return new FeignClientHandler(ErrorStatus.FEIGN_CLIENT_ERROR);
        }
    }
}
