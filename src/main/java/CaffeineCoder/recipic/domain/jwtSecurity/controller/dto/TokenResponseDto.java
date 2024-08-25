package CaffeineCoder.recipic.domain.jwtSecurity.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
}
