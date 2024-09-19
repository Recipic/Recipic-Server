package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.brand.api.BrandService;
import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeResponseDto;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import CaffeineCoder.recipic.domain.user.application.UserService;
import CaffeineCoder.recipic.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeRankService {
    private final RecipeRepository recipeRepository;
    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public List<RecipeResponseDto> getTop5NormalRecipes() {
        List<Integer> topRecipeIds = scrapRepository.findTop5NormalRecipesByScrapCount(PageRequest.of(0, 5));

        List<RecipeResponseDto> topRecipes = new ArrayList<>();
        for (int recipeId : topRecipeIds) {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
            User user = userService.findUser(recipe.getUserId());

            int scrapCount = scrapRepository.countByRecipeId(recipeId);
            String brandName = recipe.getBrandName();

            topRecipes.add(RecipeResponseDto.fromEntity(recipe, user, brandName, scrapCount, 0));
        }

        return topRecipes;
    }

    public List<RecipeResponseDto> getTop5CelebrityRecipes() {

        List<Integer> topRecipeIds = scrapRepository.findTop5CelebrityRecipesByScrapCount(PageRequest.of(0, 5));

        List<RecipeResponseDto> topRecipes = new ArrayList<>();

        for (int recipeId : topRecipeIds) {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
            int scrapCount = scrapRepository.countByRecipeId(recipe.getRecipeId());
            int commentCount = commentRepository.countByRecipeId(recipe.getRecipeId());
            User user = userService.findUser(recipe.getUserId());

            String brandName = recipe.getBrandName();

            topRecipes.add(RecipeResponseDto.fromEntity(recipe, user, brandName, scrapCount, commentCount));
        }

        return topRecipes;
    }
}
