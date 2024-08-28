package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.comment.dto.CommentDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecipeDetailResponseDto {
    Integer recipeId;
    String userNickName;
    String userProfileImageUrl;
    String brandName;
    String title;
    String description;
    String thunbnailUrl;
    String isCelebrity;
    String createdAt;
    String status;
    Boolean isScrapped;
    int scrapCount;
    List<IncludeIngredientDto> IncludeIngredients;



    @Builder
    public RecipeDetailResponseDto(Integer recipeId, String userNickName, String userProfileImageUrl, String brandName, String title, String description, String thunbnailUrl, String isCelebrity, String createdAt, String status,boolean isScrapped, int scrapCount, List<IncludeIngredientDto> IncludeIngredients) {
        this.recipeId = recipeId;
        this.userNickName = userNickName;
        this.userProfileImageUrl = userProfileImageUrl;
        this.brandName = brandName;
        this.title = title;
        this.description = description;
        this.thunbnailUrl = thunbnailUrl;
        this.isCelebrity = isCelebrity;
        this.createdAt = createdAt;
        this.status = status;
        this.isScrapped = isScrapped;
        this.scrapCount = scrapCount;
        this.IncludeIngredients = IncludeIngredients;
    }
}
