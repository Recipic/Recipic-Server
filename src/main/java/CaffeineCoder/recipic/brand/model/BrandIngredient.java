package CaffeineCoder.recipic.brand.model;

import jakarta.persistence.*;

@Entity
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


    // Getter와 Setter

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    // Constructor (기본 생성자 포함)
}


