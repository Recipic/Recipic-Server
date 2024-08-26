package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;

import java.sql.Timestamp;

public record RecipeResponseDto(
    Integer recipeId,
    Long userId,
    Integer brandId,
    String title,
    String description,
    String imageUrl,
    Boolean isCelebrity,
    Timestamp createdAt,
    Integer status,
    Integer scrapCount
) {
    public static RecipeResponseDto of(Integer recipeId, Long userId, Integer brandId, String title, String description, String imageUrl, Boolean isCelebrity, Timestamp createdAt, Integer status, Integer scrapCount) {
        return new RecipeResponseDto(recipeId, userId, brandId, title, description, imageUrl, isCelebrity, createdAt, status, scrapCount);
    }

    //추후 댓글 개수도 추가
    public static RecipeResponseDto fromEntity(Recipe recipe, int scrapCount) {
        return new RecipeResponseDto(
                recipe.getRecipeId(),
                recipe.getUserId(),
                recipe.getBrandId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getImageUrl(),
                recipe.getIsCelebrity(),
                recipe.getCreatedAt(),
                recipe.getStatus(),
                scrapCount
        );
    }
    
    public static RecipeResponseDto fromDto(RecipeDto recipeDto, int scrapCount) {
        return new RecipeResponseDto(
                recipeDto.recipeId(),
                recipeDto.userId(),
                recipeDto.brandId(),
                recipeDto.title(),
                recipeDto.description(),
                recipeDto.imageUrl(),
                recipeDto.isCelebrity(),
                recipeDto.createdAt(),
                recipeDto.status(),
                scrapCount
        );
    }
}
