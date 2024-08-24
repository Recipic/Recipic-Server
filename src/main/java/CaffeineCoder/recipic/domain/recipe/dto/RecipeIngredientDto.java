package CaffeineCoder.recipic.domain.recipe.dto;

public class RecipeIngredientDto {
    private final Long ingredientId;
    private final Integer count;

    // 생성자 추가
    public RecipeIngredientDto(Long ingredientId, Integer count) {
        this.ingredientId = ingredientId;
        this.count = count;
    }

    // Getter 메서드 추가
    public Long getIngredientId() {
        return ingredientId;
    }

    public Integer getCount() {
        return count;
    }
}

