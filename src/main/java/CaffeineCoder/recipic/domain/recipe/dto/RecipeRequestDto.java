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

    private String recipeId;
    private String userId;
    private Integer brandId;
    private String title;
    private String thumbnailUrl;
    private List<SelectedRecipeDto> selectedRecipes;
    private String description;
    private Boolean isCelebrity;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SelectedRecipeDto {
        private Integer ingredientId;
        private Integer count;
    }
}
