package CaffeineCoder.recipic.domain.jwtSecurity.config;

import CaffeineCoder.recipic.domain.jwtSecurity.jwt.JwtAccessDeniedHandler;
import CaffeineCoder.recipic.domain.jwtSecurity.jwt.JwtAuthenticationEntryPoint;
import CaffeineCoder.recipic.domain.jwtSecurity.jwt.JwtFilter;
import CaffeineCoder.recipic.domain.jwtSecurity.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/h2-console/**", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            // CSRF 설정 Disable
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .headers((header) -> header.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
                .sessionManagement((ssessionMng) -> ssessionMng.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((registry) ->
                        registry
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/page/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()

                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt 변경사항

        return http.build();


    }
}
