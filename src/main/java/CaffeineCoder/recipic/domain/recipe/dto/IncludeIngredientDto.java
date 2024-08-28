package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IncludeIngredientDto {
    private Ingredient ingredient;
    private Integer count;

    @Builder
    public IncludeIngredientDto(Ingredient ingredient, Integer count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    // Getters and Setters
}

