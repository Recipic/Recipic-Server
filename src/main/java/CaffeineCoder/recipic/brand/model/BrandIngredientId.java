package CaffeineCoder.recipic.brand.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BrandIngredientId implements Serializable {

    private Integer brandId;
    private Integer ingredientId;

}
