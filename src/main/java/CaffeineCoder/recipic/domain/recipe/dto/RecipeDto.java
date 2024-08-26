package CaffeineCoder.recipic.domain.recipe.dto;

import java.sql.Timestamp;

public record RecipeDto(
        Integer recipeId,
        Long userId,
        Integer brandId,
        String title,
        String description,
        String imageUrl,
        Boolean isCelebrity,
        Timestamp createdAt,
        Integer status
) {

    public static RecipeDto of(Integer recipeId, Long userId, Integer brandId, String title, String description, String imageUrl, Boolean isCelebrity, Timestamp createdAt, Integer status) {
        return new RecipeDto(recipeId, userId, brandId, title, description, imageUrl, isCelebrity, createdAt, status);
    }
}

