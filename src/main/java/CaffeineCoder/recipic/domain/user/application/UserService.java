package CaffeineCoder.recipic.domain.user.application;

import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.domain.user.dto.UserRequestDto;
import CaffeineCoder.recipic.domain.user.dto.UserResponseDto;
import CaffeineCoder.recipic.global.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserResponseDto findMemberInfoById(Long memberId) {

        return userRepository.findById(memberId)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public UserResponseDto findMemberInfoByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    public User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponseDto updateUser(UserRequestDto userRequestDto, MultipartFile profileImage) {
        User user = findUser(SecurityUtil.getCurrentMemberId());

        String uuid = null;
        try {
            if (!profileImage.isEmpty()) {
                uuid = imageService.uploadImage(profileImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (uuid == null) {
            uuid = user.getProfileImageUrl();
        }

        user.update(userRequestDto.getNickName(), uuid, userRequestDto.getDescription());

        return UserResponseDto.of(user);
    }

}
