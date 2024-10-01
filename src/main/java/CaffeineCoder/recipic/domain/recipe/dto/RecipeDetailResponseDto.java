package CaffeineCoder.recipic.domain.recipe.dto;

import CaffeineCoder.recipic.domain.comment.dto.CommentDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecipeDetailResponseDto {
    private Integer recipeId;
    private String userNickName;
    private String userProfileImageUrl;
    private String brandName;
    private String title;
    private String description;
    private String thunbnailUrl;
    private Boolean isCelebrity;
    private String createdAt;
    private String status;
    private Boolean isScrapped;
    private int scrapCount;
    private String baseIngredient;
    private List<IncludeIngredientDto> includeIngredients;

    @Builder
    public RecipeDetailResponseDto(
            Integer recipeId, String userNickName, String userProfileImageUrl,
            String brandName, String title, String description, String thunbnailUrl,
            Boolean isCelebrity, String createdAt, String status, boolean isScrapped,
            int scrapCount, List<IncludeIngredientDto> includeIngredients,
            String baseIngredient
    ) {
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
        this.includeIngredients = includeIngredients;
        this.baseIngredient = baseIngredient;
    }
}
