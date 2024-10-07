package CaffeineCoder.recipic.domain.jwtSecurity.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@RequiredArgsConstructor
public class RefreshToken implements Serializable {
    private String key;
    private String token;
    private Long userId;

    @JsonCreator
    public RefreshToken(
            @JsonProperty("key") String key,
            @JsonProperty("token") String token,
            @JsonProperty("userId") Long userId) {
        this.key = key;
        this.token = token;
        this.userId = userId;
    }


    public RefreshToken updateValue(String newToken) {
        this.token = newToken;
        return this;
    }

}

