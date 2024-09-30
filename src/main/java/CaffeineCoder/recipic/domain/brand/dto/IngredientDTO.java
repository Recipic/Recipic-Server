package CaffeineCoder.recipic.domain.brand.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Builder // @Builder 어노테이션 추가
public class IngredientDTO {

    private final Integer ingredientId;  // 재료 ID
    private final String ingredientName;  // 재료명
    private final Long quantity;  // 양
    private final String unit;  // 단위
    private final Integer cost;  // 비용
    private final Double calorie;  // 칼로리
    private final BaseIngredientDTO baseIngredient;  // BaseIngredientDTO 추가

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("ingredientId", ingredientId);
        map.put("ingredientName", ingredientName);
        map.put("quantity", quantity);
        map.put("unit", unit);
        map.put("calorie", calorie);
        map.put("cost", cost);
        return map;
    }
}
