package CaffeineCoder.recipic.domain.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequestDto {
    private Integer recipeId;
    private String title;
    private String description;
    private Boolean isCelebrity;
    private String brandName;
    private Integer baseIngredientId;
    private List<SelectedIngredientDto> selectedIngredients; // 선택된 재료

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SelectedIngredientDto {
        private Integer ingredientId;
        private Integer count;
    }
}
