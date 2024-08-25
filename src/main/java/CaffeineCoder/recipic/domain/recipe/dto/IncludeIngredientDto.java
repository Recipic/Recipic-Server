package CaffeineCoder.recipic.domain.recipe.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IncludeIngredientDto {
    Integer ingredientId;
    Integer count;

    @Builder
    public IncludeIngredientDto(Integer ingredientId, Integer count) {
        this.ingredientId = ingredientId;
        this.count = count;
    }

    // Getters and Setters
}
