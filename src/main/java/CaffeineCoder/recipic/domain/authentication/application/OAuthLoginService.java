package CaffeineCoder.recipic.domain.authentication.application;

import CaffeineCoder.recipic.domain.authentication.domain.AuthTokensGenerator;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthInfoResponse;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthLoginParams;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.RequestOAuthInfoService;
import CaffeineCoder.recipic.domain.authentication.infra.kakao.admin.AdminInfoResponse;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import CaffeineCoder.recipic.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final RedisUtil redisUtil;
    private final long ACCESS_TOKEN_EXPIRATION = 3600000;


    public TokenDto login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);

        saveUserInfoToRedis(oAuthInfoResponse);

        return authTokensGenerator.generate(memberId);
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getUserId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        User user = User.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickName(oAuthInfoResponse.getNickname())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .profileImageUrl(oAuthInfoResponse.getProfileImageUrl())
                .build();

        return userRepository.save(user).getUserId();
    }

    private void saveUserInfoToRedis(OAuthInfoResponse oAuthInfoResponse) {
        System.out.printf("OAuthInfoResponse: %s\n", oAuthInfoResponse);
        System.out.println("Redis에 저장합니다.");
        System.out.printf("key: %s, value: %s\n", "USER: " + oAuthInfoResponse.getEmail(), oAuthInfoResponse.getAccessToken());
        String key = "USER: " + oAuthInfoResponse.getEmail(); // Redis에 저장할 키
        String value = oAuthInfoResponse.getAccessToken(); // 저장할 값 (닉네임을 예시로 사용)

        redisUtil.setValuesWithTimeout(key, value, ACCESS_TOKEN_EXPIRATION); // 1시간 동안 유효
    }

    public TokenDto issueAdmin() {
        AdminInfoResponse.AdminProfile profile = AdminInfoResponse.AdminProfile.builder()
                .profileImageURL("https://ogq-sticker-global-cdn-z01.afreecatv.com/sticker/60f884df5b12c/1.png")
                .nickname("관리자")
                .build();

        AdminInfoResponse.AdminAccount adminAccount = AdminInfoResponse.AdminAccount.builder()
                .profile(profile)
                .email("test@example.com")
                .build();

        AdminInfoResponse adminInfoResponse = AdminInfoResponse.builder()
                .adminAccount(adminAccount)
                .build();



        return authTokensGenerator.generateAdmin(findOrCreateMember(adminInfoResponse));
    }
}
