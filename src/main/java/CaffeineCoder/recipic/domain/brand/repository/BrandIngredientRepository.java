package CaffeineCoder.recipic.domain.brand.repository;

import CaffeineCoder.recipic.domain.brand.domain.Brand;
import CaffeineCoder.recipic.domain.brand.domain.BrandIngredient;
import CaffeineCoder.recipic.domain.brand.domain.BrandIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandIngredientRepository extends JpaRepository<BrandIngredient, BrandIngredientId> {
    List<BrandIngredient> findByBrand(Brand brand);
}
