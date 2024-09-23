package CaffeineCoder.recipic.domain.brand.repository;

import CaffeineCoder.recipic.domain.brand.domain.BaseIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseIngredientRepository extends JpaRepository<BaseIngredient, Integer> {

    // BaseIngredient 이름으로 찾기
    Optional<BaseIngredient> findByIngredientName(String ingredientName);
    List<BaseIngredient> findByBrand(Brand brand);
}
