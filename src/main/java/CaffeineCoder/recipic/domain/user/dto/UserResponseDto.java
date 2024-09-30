package CaffeineCoder.recipic.domain.user.dto;

import CaffeineCoder.recipic.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String nickName;
    private String profileImageUrl;
    private String email;
    private String description;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getNickName(),
                user.getProfileImageUrl(),
                user.getEmail(),
                user.getDescription());
    }



}

