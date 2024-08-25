package CaffeineCoder.recipic.global.util;

import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class JwtUtils {
    public static void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);  // JS로 쿠키 접근 못하게 설정 (보안 강화)
        cookie.setPath("/");  // 쿠키가 유효한 경로 설정
        cookie.setMaxAge(7 * 24 * 60 * 60);  // 쿠키의 만료 시간 설정 (예: 7일)
        response.addCookie(cookie);
    }

    public static TokenResponseDto setJwtResponse(HttpServletResponse response, TokenDto tokenDto) {
        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                .build();

        setRefreshTokenInCookie(response, tokenDto.getRefreshToken());

        return tokenResponseDto;
    }
}
