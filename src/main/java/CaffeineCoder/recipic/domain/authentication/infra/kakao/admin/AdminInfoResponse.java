package CaffeineCoder.recipic.domain.authentication.infra.kakao.admin;

import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthInfoResponse;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminInfoResponse implements OAuthInfoResponse {

    @JsonProperty("Admin_account")
    public AdminAccount adminAccount;

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdminAccount {
        public AdminProfile profile;
        public String email;
    }

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdminProfile {
        @JsonProperty("profile_image_url") // JSON에서 프로필 이미지 URL을 매핑할 때 사용하는 속성 이름
        public String profileImageURL;
        public String nickname;
    }

    @Override
    public String getEmail() {
        return adminAccount.email;
    }

    @Override
    public String getNickname() {
        return adminAccount.profile.nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getProfileImageUrl() {
        return adminAccount.profile.profileImageURL;
    }

    @Override
    public String getAccessToken() {
        return null;
    }
}
