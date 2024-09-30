package CaffeineCoder.recipic.domain.user.domain;

import CaffeineCoder.recipic.domain.authentication.domain.oauth.OAuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "description", nullable = true)
    @Size(max = 100)
    private String description;

    public void update(String nickName, String profileImageUrl, String description) {
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
    }

}
