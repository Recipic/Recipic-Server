package CaffeineCoder.recipic.domain.brand.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseIngredientDTO {
    private Integer baseIngredientId;
    private String ingredientName;
}
