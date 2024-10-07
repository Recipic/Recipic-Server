package CaffeineCoder.recipic.domain.jwtSecurity.service;

import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenRequestDto;
import CaffeineCoder.recipic.domain.jwtSecurity.entity.RefreshToken;
import CaffeineCoder.recipic.domain.jwtSecurity.jwt.TokenProvider;
import CaffeineCoder.recipic.domain.jwtSecurity.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtParserBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static CaffeineCoder.recipic.domain.jwtSecurity.jwt.JwtTokenExpiration.ACCESS_TOKEN_EXPIRE_TIME;
import static CaffeineCoder.recipic.domain.jwtSecurity.jwt.JwtTokenExpiration.REFRESH_TOKEN_EXPIRE_TIME;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication  authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken, REFRESH_TOKEN_EXPIRE_TIME);


        // 토큰 발급
        return tokenDto;
    }


    public long extractAccessExpiration() {
        return ACCESS_TOKEN_EXPIRE_TIME;
    }


}
