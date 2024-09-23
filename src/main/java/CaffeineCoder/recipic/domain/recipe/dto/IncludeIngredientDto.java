package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.brand.domain.BaseIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IncludeIngredientDto {
    private Ingredient ingredient;
    private BaseIngredient baseIngredient;
    private Integer count;

    @Builder
    public IncludeIngredientDto(Ingredient ingredient, BaseIngredient baseIngredient, Integer count) {
        this.ingredient = ingredient;
        this.baseIngredient = baseIngredient;
        this.count = count;
    }
}
