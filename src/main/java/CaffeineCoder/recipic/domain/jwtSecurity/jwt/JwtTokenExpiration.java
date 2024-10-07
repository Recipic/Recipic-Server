package CaffeineCoder.recipic.domain.jwtSecurity.jwt;

public class JwtTokenExpiration {
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 1일
    public static final long ADMIN_ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 20; // 20일
}

