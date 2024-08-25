package CaffeineCoder.recipic.domain.authentication.api;

import CaffeineCoder.recipic.domain.authentication.infra.kakao.KakaoLoginParams;
import CaffeineCoder.recipic.domain.authentication.infra.naver.NaverLoginParams;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenDto> loginKakao(@RequestBody KakaoLoginParams params,HttpServletResponse response) {
        TokenDto tokenDto = oAuthLoginService.login(params);
        setRefreshTokenInCookie(response, tokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/naver")
    public ResponseEntity<TokenDto> loginNaver(@RequestBody NaverLoginParams params,HttpServletResponse response) {
        TokenDto tokenDto = oAuthLoginService.login(params);
        setRefreshTokenInCookie(response, tokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/admin")
    public ResponseEntity<TokenDto> issueAdmin(HttpServletResponse response) {
        TokenDto tokenDto = oAuthLoginService.issueAdmin();
        setRefreshTokenInCookie(response, tokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }

    private void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);  // JS로 쿠키 접근 못하게 설정 (보안 강화)
        cookie.setPath("/");  // 쿠키가 유효한 경로 설정
        cookie.setMaxAge(7 * 24 * 60 * 60);  // 쿠키의 만료 시간 설정 (예: 7일)
        response.addCookie(cookie);
    }


}
