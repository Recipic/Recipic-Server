package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.user.domain.User;

import java.sql.Timestamp;

public record RecipeResponseDto(
    Integer recipeId,
    String userNickName,
    String userProfileImageUrl,
    String title,
    String thumbnailUrl,
    String brandName,
    String description,
    Boolean isCelebrity,
    Timestamp createdAt,
    Integer status,
    Integer scrapCount,
    Integer commentCount
) {
    public static RecipeResponseDto of(Integer recipeId, String userName,String userProfileImageUrl, String title, String thumbnailUrl,String brandName,String description, Boolean isCelebrity, Timestamp createdAt, Integer status, Integer scrapCount, Integer commentCount) {
        return new RecipeResponseDto(recipeId, userName,userProfileImageUrl, title,"https://storage.googleapis.com/recipick-image-bucket/"+thumbnailUrl,brandName, description, isCelebrity, createdAt, status, scrapCount, commentCount);
    }

    //추후 댓글 개수도 추가
    public static RecipeResponseDto fromEntity(Recipe recipe, User user,String brandName, int scrapCount, int commentCount) {
        return new RecipeResponseDto(
                recipe.getRecipeId(),
                user.getNickName(),
                user.getProfileImageUrl(),
                recipe.getTitle(),
                "https://storage.googleapis.com/recipick-image-bucket/"+recipe.getImageUrl(),
                brandName,
                recipe.getDescription(),
                recipe.getIsCelebrity(),
                recipe.getCreatedAt(),
                recipe.getStatus(),
                scrapCount,
                commentCount
        );
    }
    
    public static RecipeResponseDto fromDto(RecipeDto recipeDto,User user,String brandName, int scrapCount, int commentCount) {
        return new RecipeResponseDto(
                recipeDto.recipeId(),
                user.getNickName(),
                user.getProfileImageUrl(),
                recipeDto.title(),
                recipeDto.description(),
                recipeDto.imageUrl(),
                brandName,
                recipeDto.isCelebrity(),
                recipeDto.createdAt(),
                recipeDto.status(),
                scrapCount,
                commentCount
        );
    }
}
