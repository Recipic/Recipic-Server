package CaffeineCoder.recipic.domain.recipe.api;

import CaffeineCoder.recipic.domain.recipe.application.RecipeRankService;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe/rank")
public class RecipeRankAPI {
    private final RecipeRankService recipeRankService;

    @GetMapping("/normal")
    public ApiResponse<?> getNormalRank() {
        return ApiUtils.success(recipeRankService.getTop5NormalRecipes());
    }

    @GetMapping("/celebrity")
    public ApiResponse<?> getCelebrityRank() {
        return ApiUtils.success(recipeRankService.getTop5CelebrityRecipes());
    }

}
