package CaffeineCoder.recipic.brand.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BrandIngredientId implements Serializable {

    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "ingredient_id")
    private Integer ingredientId;

}
