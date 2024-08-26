package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeResponseDto;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
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

    public List<?> getNormalRank() {
        return recipeRepository.findAll();
    }

    public List<RecipeResponseDto> getTop5NormalRecipes() {

        List<Integer> topRecipeIds = scrapRepository.findTop5NormalRecipesByScrapCount(PageRequest.of(0, 5));

        List<RecipeResponseDto> topRecipes = new ArrayList<>();

        for(int i=0; i<topRecipeIds.size(); i++){
            Recipe recipe = recipeRepository.findById(topRecipeIds.get(i)).orElseThrow(() -> new RuntimeException("Recipe not found"));
            int scrapCount = scrapRepository.countByRecipeId(recipe.getRecipeId());
            int commentCount = commentRepository.countByRecipeId(recipe.getRecipeId());
            topRecipes.add(RecipeResponseDto.fromEntity(recipe, scrapCount,commentCount));
        }


        return topRecipes;
    }

    public List<RecipeResponseDto> getTop5CelebrityRecipes() {

        List<Integer> topRecipeIds = scrapRepository.findTop5CelebrityRecipesByScrapCount(PageRequest.of(0, 5));

        List<RecipeResponseDto> topRecipes = new ArrayList<>();

        for(int i=0; i<topRecipeIds.size(); i++){
            Recipe recipe = recipeRepository.findById(topRecipeIds.get(i)).orElseThrow(() -> new RuntimeException("Recipe not found"));
            int scrapCount = scrapRepository.countByRecipeId(recipe.getRecipeId());
            int commentCount = commentRepository.countByRecipeId(recipe.getRecipeId());
            topRecipes.add(RecipeResponseDto.fromEntity(recipe, scrapCount,commentCount));
        }

        return topRecipes;
    }

}
