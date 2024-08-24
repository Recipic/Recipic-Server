package CaffeineCoder.recipic.domain.authentication.domain;

import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import CaffeineCoder.recipic.domain.jwtSecurity.entity.RefreshToken;
import CaffeineCoder.recipic.domain.jwtSecurity.jwt.TokenProvider;
import CaffeineCoder.recipic.domain.jwtSecurity.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenDto generate(Long memberId) {

        String subject = memberId.toString();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    public TokenDto generateAdmin() {

        String subject = "admin";
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
        TokenDto tokenDto = tokenProvider.generateAdminTokenDto(authentication);


        return tokenDto;
    }

}
