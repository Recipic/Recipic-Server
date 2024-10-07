package CaffeineCoder.recipic.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setBlackList(String key, Object o, Duration minutes) {
        redisTemplate.opsForValue().set(key, o, minutes);
    }

    /**
     * 키를 이용한 값 확인
     */
    public Object getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 만료시간 설정 -> 자동삭제
     */
    @Transactional
    public void setValuesWithTimeout(String key, String value, long timeout){
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
            System.out.println("Stored in Redis: " + key + " -> " + value);
        } catch (Exception e) {
            System.err.println("Failed to store in Redis: " + e.getMessage());
        }
    }

    /**
     * 키 삭제
     */
    @Transactional
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}

