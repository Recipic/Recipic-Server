package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import CaffeineCoder.recipic.domain.brand.repository.IngredientRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeIngredientRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredientId;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeDto;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final BrandRepository brandRepository;
    private final IngredientRepository ingredientRepository;
    private final ScrapRepository scrapRepository;


    public void registerRecipe(Map<String, Object> recipeData) {
        // Recipe 엔티티 생성
        Recipe recipe = Recipe.builder()
                .userId(Long.valueOf((String) recipeData.get("userId")))
                .brandId((Integer) recipeData.get("brandId"))
                .title((String) recipeData.get("title"))
                .description((String) recipeData.get("description"))
                .imageUrl((String) recipeData.get("thumbnailUrl"))
                .isCelebrity((Boolean) recipeData.get("isCelebrity"))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(1)
                .build();

        // Recipe 저장
        Recipe savedRecipe = recipeRepository.save(recipe);

        // RecipeIngredient 저장
        List<Map<String, Object>> selectedIngredients = (List<Map<String, Object>>) recipeData.get("selectedRecipes");
        List<RecipeIngredient> recipeIngredients = selectedIngredients.stream().map(ingredientData -> {
            RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                    (Integer) ingredientData.get("ingredientId"),
                    savedRecipe.getRecipeId()
            );

            return RecipeIngredient.builder()
                    .id(recipeIngredientId)
                    .recipe(savedRecipe)
                    .ingredient(ingredientRepository.findById((Integer) ingredientData.get("ingredientId"))
                            .orElseThrow(() -> new RuntimeException("Ingredient not found")))
                    .count((Integer) ingredientData.get("count"))
                    .build();
        }).collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);
    }

    public RecipeDto getRecipeDetail(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        int scrapCount = scrapRepository.countByRecipeId(recipeId);

        return RecipeDto.fromEntity(recipe, scrapCount);
    }




}