package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BrandIngredient {

    @EmbeddedId
    private BrandIngredientId id;

    @ManyToOne
    @MapsId("brandId")
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;


    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

}
