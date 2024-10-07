package CaffeineCoder.recipic.domain.authentication.domain.oauth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
    String getProfileImageUrl();
    String getAccessToken();
}
