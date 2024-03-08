package com.sm.project.coolsms;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;


@Repository
@RequiredArgsConstructor
public class RedisUtil {

    private final String PREFIX = "sms:";
    private final int LIMIT_TIME = 3 * 60; //인증 제한 시간

    private final StringRedisTemplate redisTemplate;

    public void createSmsCertification(String phone, String certificationNum) {
        redisTemplate.opsForValue()
                .set(PREFIX + phone, certificationNum, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getSmsCertification(String phone) {
        return redisTemplate.opsForValue().get(PREFIX + phone);
    }

    public void removeSmsCertification(String phone) {
        redisTemplate.delete(PREFIX + phone);
    }

    public boolean hasKey(String phone) {
        return redisTemplate.hasKey(PREFIX + phone);
    }
}
