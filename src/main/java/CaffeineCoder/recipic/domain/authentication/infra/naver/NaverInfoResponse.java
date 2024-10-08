package CaffeineCoder.recipic.domain.authentication.infra.naver;

import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthInfoResponse;
import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String email;
        private String nickname;
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getNickname() {
        return response.nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public String getProfileImageUrl() {
        return null;
    }

    @Override
    public String getAccessToken() {
        return null;
    }
}
