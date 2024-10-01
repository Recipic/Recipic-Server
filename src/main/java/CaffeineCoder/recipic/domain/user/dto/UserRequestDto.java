package CaffeineCoder.recipic.domain.user.dto;

import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String nickName;
    private String description;

    public static UserRequestDto of(User user) {
        return new UserRequestDto(user.getNickName(), user.getDescription());
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


}
