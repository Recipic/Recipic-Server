package CaffeineCoder.recipic.domain.user.application;

import CaffeineCoder.recipic.domain.recipe.application.RecipeService;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeResponseDto;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserScrapService {
    private final ScrapRepository scrapRepository;
    private final RecipeService recipeService;

    public List<RecipeResponseDto> getUserQueriedScrapedRecipes(String keyword, int page, int size, Long userId) {
        List<Integer> recipeIds = scrapRepository.findRecipeIdsByUserId(userId);

        if (recipeIds.isEmpty()) {
            // 스크랩한 레시피가 없는 경우 빈 리스트 반환
            return List.of();
        }

        // 스크랩한 레시피가 있는 경우
        return recipeService.getUserQueriedScrapedRecipes(keyword, page, size, recipeIds);

    }
}
