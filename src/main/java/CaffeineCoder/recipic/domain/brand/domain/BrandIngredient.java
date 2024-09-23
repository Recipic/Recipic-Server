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
    @MapsId("baseIngredientId")
    @JoinColumn(name = "baseingredient_id")
    private BaseIngredient baseIngredient;

    public BrandIngredient(Brand brand, BaseIngredient baseIngredient) {
        this.brand = brand;
        this.baseIngredient = baseIngredient;
        this.id = new BrandIngredientId(brand.getBrandId(), baseIngredient.getBaseIngredientId());
    }
}
