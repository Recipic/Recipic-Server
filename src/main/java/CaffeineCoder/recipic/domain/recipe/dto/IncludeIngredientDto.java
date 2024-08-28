package CaffeineCoder.recipic.domain.recipe.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IncludeIngredientDto {
    String ingredientName;
    Integer count;

    @Builder
    public IncludeIngredientDto(String ingredientName, Integer count) {
        this.ingredientName = ingredientName;
        this.count = count;
    }

    // Getters and Setters
}
