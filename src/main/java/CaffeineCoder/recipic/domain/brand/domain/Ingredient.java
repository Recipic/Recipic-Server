package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
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

    @Builder
    public Ingredient(String ingredientName, String quantity, String unit, Integer cost, Double calorie) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.cost = cost;
        this.calorie = calorie;
    }

    // 기본 생성자 추가
    protected Ingredient() {
    }
}
