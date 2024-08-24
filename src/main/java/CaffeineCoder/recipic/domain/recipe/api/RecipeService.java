package CaffeineCoder.recipic.domain.recipe.api;

import CaffeineCoder.recipic.domain.recipe.dao.*;
import CaffeineCoder.recipic.domain.recipe.domain.*;
import CaffeineCoder.recipic.recipe.dao.*;
import CaffeineCoder.recipic.recipe.domain.*;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeDto;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeIngredientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Transactional
    public Recipe registerRecipe(RecipeDto recipeDto) {
        User user = userRepository.findById(recipeDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Brand brand = brandRepository.findById(recipeDto.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));

        Recipe recipe = new Recipe(
                user,
                brand,
                recipeDto.getTitle(),
                recipeDto.getDescription(),
                recipeDto.getImageUrl(),
                recipeDto.getIsCelebrity(),
                recipeDto.getStatus()
        );

        recipeRepository.save(recipe);

        for (RecipeIngredientDto ingredientDto : recipeDto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ingredient ID"));

            RecipeIngredient recipeIngredient = new RecipeIngredient(
                    recipe,
                    ingredient,
                    ingredientDto.getCount()
            );

            recipeIngredientRepository.save(recipeIngredient);
        }

        return recipe;
    }

}
