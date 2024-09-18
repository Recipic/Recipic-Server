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

    public void setBaseIngredient(BaseIngredient baseIngredient) {
        this.baseIngredient = baseIngredient;
        if (this.brand != null) {
            this.id = new BrandIngredientId(this.brand.getBrandId(), baseIngredient.getBaseIngredientId());
        }
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
        if (this.baseIngredient != null) {
            this.id = new BrandIngredientId(brand.getBrandId(), this.baseIngredient.getBaseIngredientId());
        }
    }
}
