package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
public class BrandIngredientId implements Serializable {

    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "baseingredient_id")
    private Integer baseIngredientId;

    public BrandIngredientId(Integer brandId, Integer baseIngredientId) {
        this.brandId = brandId;
        this.baseIngredientId = baseIngredientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandIngredientId that = (BrandIngredientId) o;
        return Objects.equals(brandId, that.brandId) &&
                Objects.equals(baseIngredientId, that.baseIngredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, baseIngredientId);
    }
}
