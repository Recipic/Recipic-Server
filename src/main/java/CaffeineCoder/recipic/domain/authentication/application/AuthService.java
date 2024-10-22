package CaffeineCoder.recipic.domain.authentication.application;

import CaffeineCoder.recipic.domain.jwtSecurity.repository.RefreshTokenRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.service.JwtService;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.user.application.UserService;
import CaffeineCoder.recipic.global.util.RedisUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.Cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import static CaffeineCoder.recipic.domain.jwtSecurity.jwt.JwtTokenExpiration.ACCESS_TOKEN_EXPIRE_TIME;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final RedisUtil redisUtil;
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String withdraw(HttpServletRequest request, HttpServletResponse response) {
        String email = invalidateToken(request);
        String socialAccessToken = null;
        if (redisUtil.getValues("USER: " + email) != null) {
            socialAccessToken = (String) redisUtil.getValues("USER: " + email);
            redisUtil.deleteValues("USER: " + email);
        }
        else{
            throw new RuntimeException("소셜 로그인 정보가 없습니다.");
        }

        // 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        kakaoUnlink(socialAccessToken, request);



        return "회원 탈퇴가 정상적으로 처리되었습니다.";
    }


    public String logout(HttpServletRequest request) {
        // 액세스/리프레시 토큰 무효화
        String email = invalidateToken(request);

        /* oauth2 access 토큰 삭제 */
        if (redisUtil.getValues("USER: " + email) != null) {
            String socialAccessToken = (String) redisUtil.getValues("USER: " + email);
            kakaoLogout(socialAccessToken);

            redisUtil.deleteValues("USER: " + email);
        }

        String responseString = email + "가 로그아웃 되었습니다.";



        return responseString;
    }

    private String invalidateToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long expiration = jwtService.extractAccessExpiration();
        String email = userService.findUserEmail(SecurityUtil.getCurrentMemberId());
        blacklistAccessToken(token);

        String id;
        try {
            id = SecurityUtil.getCurrentMemberId().toString();
        } catch (Exception e) {
            throw new RuntimeException("현재 사용자 ID를 가져오는 데 실패했습니다.", e);
        }


        Base64.Decoder decoder = Base64.getDecoder();
        final String[] splitJwt = token.split("\\.");
        final String payloadStr = new String(decoder.decode(splitJwt[1].replace('-', '+').replace('_', '/').getBytes()));



        // 리프레시 토큰 삭제
        if (refreshTokenRepository.findByKey(id).isPresent()) {
            refreshTokenRepository.deleteByKey(id);
        }



        return email;
    }


    public void blacklistAccessToken(String token) {
        Duration duration = Duration.ofMillis(ACCESS_TOKEN_EXPIRE_TIME);
        // 블랙리스트에 토큰을 등록하고 만료 시간을 설정
        String blacklistKey = "BLACKLISTED_ACCESS_TOKEN:" + token;
        redisUtil.setBlackList(blacklistKey,"invalid",duration);;
    }

    public void kakaoLogout(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void kakaoUnlink(String accessToken, HttpServletRequest request) {

        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
