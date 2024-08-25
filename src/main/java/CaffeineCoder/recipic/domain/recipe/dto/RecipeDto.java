package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;

public record RecipeDto (
    String recipeId,
    String userId,
    String brandId,
    String title,
    String description,
    String imageUrl,
    String isCelebrity,
    String createdAt,
    String status
) {
    public static RecipeDto of(String recipeId, String userId, String brandId, String title, String description, String imageUrl, String isCelebrity, String createdAt, String status) {
        return new RecipeDto(recipeId, userId, brandId, title, description, imageUrl, isCelebrity, createdAt, status);
    }

    public static RecipeDto fromEntity(Recipe recipe) {
        return new RecipeDto(
                recipe.getRecipeId().toString(),
                recipe.getUserId().toString(),
                recipe.getBrandId().toString(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getImageUrl(),
                recipe.getIsCelebrity().toString(),
                recipe.getCreatedAt().toString(),
                recipe.getStatus().toString()
        );
    }
}
