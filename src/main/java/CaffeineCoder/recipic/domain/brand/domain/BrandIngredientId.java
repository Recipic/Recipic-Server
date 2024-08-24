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
    private Integer brandId; //Long에서 Integer로 수정

    @Column(name = "ingredient_id")
    private Integer ingredientId;

    public BrandIngredientId(Integer brandId, Integer ingredientId) {
        this.brandId = brandId;
        this.ingredientId = ingredientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandIngredientId that = (BrandIngredientId) o;
        return Objects.equals(brandId, that.brandId) &&
                Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, ingredientId);
    }
}
