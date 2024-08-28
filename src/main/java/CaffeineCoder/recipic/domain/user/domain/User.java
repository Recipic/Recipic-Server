package CaffeineCoder.recipic.domain.user.domain;

import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String email;

    private String nickName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "o_auth_provider")
    private OAuthProvider oAuthProvider;

    @Builder
    public User(String email, String nickName,String profileImageUrl, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.oAuthProvider = oAuthProvider;
    }
}
