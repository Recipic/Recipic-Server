package CaffeineCoder.recipic.domain.authentication.api;

import CaffeineCoder.recipic.domain.authentication.application.OAuthLoginService;
import CaffeineCoder.recipic.domain.authentication.infra.kakao.KakaoLoginParams;
import CaffeineCoder.recipic.domain.authentication.infra.naver.NaverLoginParams;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenResponseDto;
import CaffeineCoder.recipic.global.util.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<?> loginKakao(@RequestBody KakaoLoginParams params,HttpServletResponse response) {
        TokenDto tokenDto = oAuthLoginService.login(params);
        TokenResponseDto tokenResponseDto = JwtUtils.setJwtResponse(response, tokenDto);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/naver")
    public ResponseEntity<?> loginNaver(@RequestBody NaverLoginParams params,HttpServletResponse response) {
        TokenDto tokenDto = oAuthLoginService.login(params);
        TokenResponseDto tokenResponseDto = JwtUtils.setJwtResponse(response, tokenDto);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> issueAdmin(HttpServletResponse response) {
        TokenDto tokenDto = oAuthLoginService.issueAdmin();
        TokenResponseDto tokenResponseDto = JwtUtils.setJwtResponse(response, tokenDto);
        return ResponseEntity.ok(tokenResponseDto);
    }



}
