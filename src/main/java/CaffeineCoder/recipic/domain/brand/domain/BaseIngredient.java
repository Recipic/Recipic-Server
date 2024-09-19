package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class BaseIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "baseingredient_id")
    private Integer baseIngredientId;

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

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    // Ingredient와의 관계 설정 (OneToMany)
    @OneToMany(mappedBy = "baseIngredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;

    public BaseIngredient(String ingredientName, Long quantity, String unit, Integer cost, Double calorie) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.cost = cost;
        this.calorie = calorie;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}
