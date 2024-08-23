package CaffeineCoder.recipic.brand.DTO;

import java.util.LinkedHashMap;
import java.util.Map;

public class IngredientDTO {

    private Integer ingredientId;  // 재료 ID
    private String name;  // 재료명
    private String quantity;  // 양
    private String unit;  // 단위
    private Integer cost;  // 비용
    private Double calorie;  // 칼로리

    public IngredientDTO(Integer ingredientId, String name, String quantity, String unit, Integer cost, Double calorie) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.cost = cost;
        this.calorie = calorie;
    }

    // Getters and Setters
    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Double getCalorie() {
        return calorie;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
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
