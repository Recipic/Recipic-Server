package CaffeineCoder.recipic.domain.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequestDto {

    private Integer recipeId;
    private String brandName;
    private String title;
    private List<SelectedRecipeDto> selectedRecipes;
    private String description;
    private Boolean isCelebrity;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SelectedRecipeDto {
        private String ingredientName;
        private Integer count;
    }
}
