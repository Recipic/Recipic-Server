package CaffeineCoder.recipic.domain.scrap.api;

import CaffeineCoder.recipic.domain.notification.application.NotificationService;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import CaffeineCoder.recipic.domain.scrap.domain.Scrap;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final NotificationService notificationService;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleScrap(Integer recipeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 사용자가 이미 이 레시피를 스크랩했는지 확인
        Optional<Scrap> existingScrap = scrapRepository.findByUserIdAndRecipeId(userId, recipeId);

        if (existingScrap.isPresent()) {
            // 이미 스크랩한 경우 스크랩 취소
            scrapRepository.deleteByUserIdAndRecipeId(userId, recipeId);
            return false;
        } else {
            // 새로운 스크랩을 추가
            Scrap scrap = Scrap.builder()
                    .userId(userId)
                    .recipeId(recipeId)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            scrapRepository.save(scrap);

            // 스크랩 알림 생성
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new RuntimeException("Recipe not found"));
            User recipeOwner = userRepository.findById(recipe.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String description = "회원님의 게시글이 스크랩되었습니다.";
            notificationService.createNotification(
                    "게시글 스크랩 알림",
                    description,
                    recipeId.longValue(),
                    recipeOwner.getUserId()
            );
            return true;
        }
    }

    public boolean isScrapped(Long userId, Integer recipeId) {
        return scrapRepository.findByUserIdAndRecipeId(userId, recipeId).isPresent();
    }
}
