package CaffeineCoder.recipic.brand.model;


import jakarta.persistence.*;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Integer ingredientId;

    @Column(name = "ingredient_name")
    private String ingredientName;  // 재료 이름

    @Column(name = "quantity")
    private String quantity;  // 양

    @Column(name = "unit")
    private String unit;  // 단위

    @Column(name = "cost")
    private Integer cost;  // 비용

    @Column(name = "calorie")
    private Double calorie;  // 칼로리

    // Getters
    public Integer getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
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

    // Setters
    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }
}
