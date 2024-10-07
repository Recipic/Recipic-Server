package CaffeineCoder.recipic.domain.jwtSecurity.config;

import CaffeineCoder.recipic.domain.jwtSecurity.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtBlacklistFilter(TokenProvider tokenProvider, RedisTemplate<String, Object> redisTemplate) {
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청에서 토큰 추출
        String token = tokenProvider.getTokenFromRequest(request);

        if (token != null) {
            // 2. 블랙리스트 확인
            String blacklistKey = "BLACKLISTED_ACCESS_TOKEN:" + token;
            Boolean isBlacklisted = redisTemplate.hasKey(blacklistKey);

            if (Boolean.TRUE.equals(isBlacklisted)) {
                // 토큰이 블랙리스트에 포함된 경우
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized - Token is blacklisted");
                return;
            }

            // 3. 토큰이 유효하고 블랙리스트에 없으면 Spring Security 인증 진행
            Authentication authentication = tokenProvider.getAuthentication(token);
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                ((UsernamePasswordAuthenticationToken) authentication).setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}