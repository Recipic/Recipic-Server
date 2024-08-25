package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import CaffeineCoder.recipic.domain.brand.repository.IngredientRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeIngredientRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredientId;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeDto;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeRequestDto;
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


    public void registerRecipe(RecipeRequestDto recipeRequestDto) {
        // Recipe 엔티티 생성
        Recipe recipe = Recipe.builder()
                .userId(Long.valueOf(recipeRequestDto.getUserId()))
                .brandId(recipeRequestDto.getBrandId())
                .title(recipeRequestDto.getTitle())
                .description(recipeRequestDto.getDescription())
                .imageUrl(recipeRequestDto.getThumbnailUrl())
                .isCelebrity(recipeRequestDto.getIsCelebrity())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(1)
                .build();

        // Recipe 저장
        Recipe savedRecipe = recipeRepository.save(recipe);

        // RecipeIngredient 저장
        List<RecipeIngredient> recipeIngredients = recipeRequestDto.getSelectedRecipes().stream()
                .map(selectedRecipe -> {
                    RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                            selectedRecipe.getIngredientId(),
                            savedRecipe.getRecipeId()
                    );

                    return RecipeIngredient.builder()
                            .id(recipeIngredientId)
                            .recipe(savedRecipe)
                            .ingredient(ingredientRepository.findById(selectedRecipe.getIngredientId())
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + selectedRecipe.getIngredientId())))
                            .count(selectedRecipe.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        // 모든 RecipeIngredient 저장
        recipeIngredientRepository.saveAll(recipeIngredients);
    }

    public RecipeDto getRecipeDetail(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        int scrapCount = scrapRepository.countByRecipeId(recipeId);

        return RecipeDto.fromEntity(recipe, scrapCount);
    }




}