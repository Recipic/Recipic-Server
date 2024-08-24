package CaffeineCoder.recipic.domain.brand.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class IngredientDTO {

    private final Integer ingredientId;  // 재료 ID
    private final String name;  // 재료명
    private final String quantity;  // 양
    private final String unit;  // 단위
    private final Integer cost;  // 비용
    private final Double calorie;  // 칼로리

    public IngredientDTO(Builder builder) {
        this.ingredientId = builder.ingredientId;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.unit = builder.unit;
        this.cost = builder.cost;
        this.calorie = builder.calorie;
    }

    public static class Builder {
        private Integer ingredientId;
        private String name;
        private String quantity;
        private String unit;
        private Integer cost;
        private Double calorie;

        // Builder 생성자를 public으로 변경
        public Builder() {}

        public Builder ingredientId(Integer ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder quantity(String quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder cost(Integer cost) {
            this.cost = cost;
            return this;
        }

        public Builder calorie(Double calorie) {
            this.calorie = calorie;
            return this;
        }

        public IngredientDTO build() {
            return new IngredientDTO(this);
        }
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getCost() {
        return cost;
    }

    public Double getCalorie() {
        return calorie;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("ingredientId", ingredientId);
        map.put("name", name);
        map.put("quantity", quantity);
        map.put("unit", unit);
        map.put("calorie", calorie);
        map.put("cost", cost);
        return map;
    }
}
