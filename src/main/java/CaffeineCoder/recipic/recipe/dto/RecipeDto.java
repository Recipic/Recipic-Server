package CaffeineCoder.recipic.recipe.dto;

import java.util.List;

public class RecipeDto {
    private final Long userId;
    private final Long brandId;
    private final String title;
    private final String description;
    private final String imageUrl;
    private final Boolean isCelebrity;
    private final Integer status;
    private final List<RecipeIngredientDto> ingredients;

    // 생성자 추가
    public RecipeDto(Long userId, Long brandId, String title, String description,
                     String imageUrl, Boolean isCelebrity, Integer status,
                     List<RecipeIngredientDto> ingredients) {
        this.userId = userId;
        this.brandId = brandId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isCelebrity = isCelebrity;
        this.status = status;
        this.ingredients = ingredients;
    }

    // Getter 메서드 추가
    public Long getUserId() {
        return userId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getIsCelebrity() {
        return isCelebrity;
    }

    public Integer getStatus() {
        return status;
    }

    public List<RecipeIngredientDto> getIngredients() {
        return ingredients;
    }
}
