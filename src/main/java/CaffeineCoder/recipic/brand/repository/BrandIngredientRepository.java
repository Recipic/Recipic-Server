package CaffeineCoder.recipic.brand.repository;

import CaffeineCoder.recipic.brand.model.Brand;
import CaffeineCoder.recipic.brand.model.BrandIngredient;
import CaffeineCoder.recipic.brand.model.BrandIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandIngredientRepository extends JpaRepository<BrandIngredient, BrandIngredientId> {
    List<BrandIngredient> findByBrand(Brand brand);
}

