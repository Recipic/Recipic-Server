package CaffeineCoder.recipic.domain.user.application;

import CaffeineCoder.recipic.domain.comment.api.CommentService;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.notification.application.NotificationService;
import CaffeineCoder.recipic.domain.recipe.application.RecipeService;
import CaffeineCoder.recipic.domain.scrap.api.ScrapService;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.domain.user.dto.UserRequestDto;
import CaffeineCoder.recipic.domain.user.dto.UserResponseDto;
import CaffeineCoder.recipic.global.error.exception.FileUploadException;
import CaffeineCoder.recipic.global.error.exception.InvalidUserException;
import CaffeineCoder.recipic.global.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
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
    private final ScrapService scrapService;
    private final CommentService commentService;
    private final NotificationService notificationService;
    private final @Lazy RecipeService recipeService;

    public UserResponseDto findMemberInfoById(Long memberId) {
        return userRepository.findById(memberId)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new InvalidUserException());
    }

    public UserResponseDto findMemberInfoByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new InvalidUserException());
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserException());
    }

    public String findUserEmail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserException())
                .getEmail();
    }

    public UserResponseDto updateUser(UserRequestDto userRequestDto, MultipartFile profileImage) {
        User user = findUser(SecurityUtil.getCurrentMemberId());
        String uuid = null;

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                uuid = "https://storage.googleapis.com/recipick-image-bucket/" + imageService.uploadImage(profileImage);
            } catch (IOException e) {
                throw new FileUploadException();
            }
        }

        if (uuid == null) {
            uuid = user.getProfileImageUrl();
        }

        if (userRequestDto == null) {
            user.profileUpdate(uuid);
            return UserResponseDto.of(user);
        }

        String nickName = userRequestDto.getNickName() != null ? userRequestDto.getNickName() : user.getNickName();
        String description = userRequestDto.getDescription() != null ? userRequestDto.getDescription() : user.getDescription();

        user.update(nickName, uuid, description);

        return UserResponseDto.of(user);
    }

    public void deleteUserRecipes(Long userId) {
        recipeService.deleteRecipesByUserId(userId);
    }

    public void withdrawUser(Long userId) {
        // 1. 댓글 삭제
        commentService.deleteCommentsByUserId(userId);

        // 2. 스크랩 삭제
        scrapService.deleteScrapsByUserId(userId);

        // 3. 레시피 삭제
        deleteUserRecipes(userId);

        // 4. 알림 삭제
        notificationService.deleteNotificationsByUserId(userId);

        // 5. 유저 삭제
        userRepository.deleteById(userId);
    }
}
