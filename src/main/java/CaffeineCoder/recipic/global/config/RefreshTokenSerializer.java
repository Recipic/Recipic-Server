package CaffeineCoder.recipic.global.config;

import CaffeineCoder.recipic.domain.jwtSecurity.entity.RefreshToken;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RefreshTokenSerializer implements RedisSerializer<RefreshToken> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(RefreshToken token) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(token);
        } catch (Exception e) {
            throw new SerializationException("Could not serialize: " + token, e);
        }
    }

    @Override
    public RefreshToken deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null; // bytes가 null 또는 비어있을 때 null 반환
        }
        try {
            return objectMapper.readValue(bytes, RefreshToken.class);
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize", e);
        }
    }

}

