package CaffeineCoder.recipic.domain.jwtSecurity.controller;


import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.AccessTokenDto;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenRequestDto;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenResponseDto;
import CaffeineCoder.recipic.domain.jwtSecurity.service.AuthService;
import CaffeineCoder.recipic.global.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
public class JwtController {
    private final AuthService authService;


    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody AccessTokenDto accessTokenRequest,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        String accessToken = accessTokenRequest.getAccessToken();


        TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        TokenDto tokenDto = authService.reissue(tokenRequestDto);

        TokenResponseDto tokenResponseDto = JwtUtils.setJwtResponse(response, tokenDto);

        return ResponseEntity.ok(tokenResponseDto);
    }
}
