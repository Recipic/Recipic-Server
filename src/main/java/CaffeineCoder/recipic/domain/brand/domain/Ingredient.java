package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Integer ingredientId;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "calorie")
    private Double calorie;

    // BaseIngredient와 관계 설정 (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "baseingredient_id", nullable = false)
    private BaseIngredient baseIngredient;

    @Builder
    public Ingredient(String ingredientName, Long quantity, String unit, Integer cost, Double calorie, BaseIngredient baseIngredient) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.cost = cost;
        this.calorie = calorie;
        this.baseIngredient = baseIngredient;
    }
}
