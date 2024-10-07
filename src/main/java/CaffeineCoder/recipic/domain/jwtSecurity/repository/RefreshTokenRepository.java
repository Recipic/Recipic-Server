package CaffeineCoder.recipic.domain.jwtSecurity.repository;

import org.springframework.data.redis.core.RedisTemplate;

import CaffeineCoder.recipic.domain.jwtSecurity.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token: ";

    @Autowired
    private RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate;

    public void save(RefreshToken refreshToken, long expirationTime) {
        refreshTokenRedisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + refreshToken.getKey(),
                refreshToken,
                expirationTime,
                TimeUnit.MILLISECONDS
        );
    }

    public Optional<RefreshToken> findByKey(String key) {
        Object token = refreshTokenRedisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + key);
        if (token != null && token instanceof RefreshToken) {
            return Optional.of((RefreshToken) token);
        }
        return Optional.empty();
    }

    public void deleteByKey(String key) {
        refreshTokenRedisTemplate.delete(REFRESH_TOKEN_PREFIX + key);
    }
}

