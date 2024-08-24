package CaffeineCoder.recipic.domain.authentication.api;

import CaffeineCoder.recipic.domain.authentication.infra.kakao.KakaoLoginParams;
import CaffeineCoder.recipic.domain.authentication.infra.naver.NaverLoginParams;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenDto> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @PostMapping("/naver")
    public ResponseEntity<TokenDto> loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }


}
