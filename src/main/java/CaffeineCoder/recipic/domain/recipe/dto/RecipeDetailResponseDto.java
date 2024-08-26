package CaffeineCoder.recipic.domain.recipe.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecipeDetailResponseDto {
    String recipeId;
    String userId;
    String brandId;
    String title;
    String description;
    String imageUrl;
    String isCelebrity;
    String createdAt;
    String status;
    int scrapCount;
    List<IncludeIngredientDto> IncludeIngredients; // Add this field

    // Constructors, Getters, Setters

    @Builder
    public RecipeDetailResponseDto(String recipeId, String userId, String brandId, String title, String description, String imageUrl, String isCelebrity, String createdAt, String status, int scrapCount, List<IncludeIngredientDto> IncludeIngredients) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.brandId = brandId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isCelebrity = isCelebrity;
        this.createdAt = createdAt;
        this.status = status;
        this.scrapCount = scrapCount;
        this.IncludeIngredients = IncludeIngredients;
    }
}
