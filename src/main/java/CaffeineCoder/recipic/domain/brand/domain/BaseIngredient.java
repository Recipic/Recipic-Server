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

    @OneToMany(mappedBy = "baseIngredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaseIngredientSize> baseIngredientSizes;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "baseIngredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;  // Ingredient와 연결

    public BaseIngredient(String ingredientName, Brand brand) {
        this.ingredientName = ingredientName;
        this.brand = brand;
    }
    public String getBaseIngredientName() {
        return this.ingredientName;
    }

}

