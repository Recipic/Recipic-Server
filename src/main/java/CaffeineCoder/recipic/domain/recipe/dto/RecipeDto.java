package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;

import java.sql.Timestamp;

public record RecipeDto(
        Integer recipeId,
        Long userId,
        String brandName, // brandId -> brandName으로 변경
        String title,
        String description,
        String imageUrl,
        Boolean isCelebrity,
        Timestamp createdAt,
        Integer status
) {

    public static RecipeDto of(Integer recipeId, Long userId, String brandName, String title, String description, String imageUrl, Boolean isCelebrity, Timestamp createdAt, Integer status) {
        return new RecipeDto(recipeId, userId, brandName, title, description, imageUrl, isCelebrity, createdAt, status);
    }

    public static RecipeDto fromEntity(Recipe recipe) {
        return new RecipeDto(
                recipe.getRecipeId(),
                recipe.getUserId(),
                recipe.getBrandName(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getImageUrl(),
                recipe.getIsCelebrity(),
                recipe.getCreatedAt(),
                recipe.getStatus()
        );
    }
}
