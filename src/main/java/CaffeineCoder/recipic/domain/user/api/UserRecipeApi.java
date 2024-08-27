package CaffeineCoder.recipic.domain.user.api;

import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.recipe.application.RecipeService;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/recipes")
public class UserRecipeApi {
    private final RecipeService recipeService;

    @GetMapping("")
    public ApiResponse<?> getUserRecipes(
            @RequestParam(value = "keyword", defaultValue = "-1") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Long userId = SecurityUtil.getCurrentMemberId();
        return ApiUtils.success(recipeService.getUserQueriedRecipes(keyword, page, size, userId));
    }

}
