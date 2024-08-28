package CaffeineCoder.recipic.domain.authentication.api;

import CaffeineCoder.recipic.domain.authentication.domain.AuthTokensGenerator;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthInfoResponse;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthLoginParams;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.RequestOAuthInfoService;
import CaffeineCoder.recipic.domain.authentication.infra.kakao.KakaoInfoResponse;
import CaffeineCoder.recipic.domain.authentication.infra.kakao.admin.AdminInfoResponse;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.controller.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public TokenDto login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);

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
