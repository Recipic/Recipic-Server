package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeDto;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeRankService {
    private final RecipeRepository recipeRepository;
    private final ScrapRepository scrapRepository;

    public List<?> getNormalRank() {
        return recipeRepository.findAll();
    }

    public List<RecipeDto> getTop5NormalRecipes() {

        List<Integer> topRecipeIds = scrapRepository.findTop5NormalRecipesByScrapCount(PageRequest.of(0, 5));

        List<RecipeDto> topRecipes = new ArrayList<>();

        for(int i=0; i<topRecipeIds.size(); i++){
            Recipe recipe = recipeRepository.findById(topRecipeIds.get(i)).orElseThrow(() -> new RuntimeException("Recipe not found"));
            int scrapCount = scrapRepository.countByRecipeId(recipe.getRecipeId());
            topRecipes.add(RecipeDto.fromEntity(recipe, scrapCount));
        }


        return topRecipes;
    }

    public List<RecipeDto> getTop5CelebrityRecipes() {

        List<Integer> topRecipeIds = scrapRepository.findTop5CelebrityRecipesByScrapCount(PageRequest.of(0, 5));

        List<RecipeDto> topRecipes = new ArrayList<>();

        for(int i=0; i<topRecipeIds.size(); i++){
            Recipe recipe = recipeRepository.findById(topRecipeIds.get(i)).orElseThrow(() -> new RuntimeException("Recipe not found"));
            int scrapCount = scrapRepository.countByRecipeId(recipe.getRecipeId());
            topRecipes.add(RecipeDto.fromEntity(recipe, scrapCount));
        }

        return topRecipes;
    }

}
