package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.brand.domain.BaseIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import CaffeineCoder.recipic.domain.brand.dto.IngredientDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IncludeIngredientDto {
    private IngredientDTO ingredient;
    private Integer count;

    @Builder
    public IncludeIngredientDto(IngredientDTO ingredient, Integer count) {
        this.ingredient = ingredient;
        this.count = count;
    }

}

