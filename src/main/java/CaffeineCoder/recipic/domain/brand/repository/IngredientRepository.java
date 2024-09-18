package CaffeineCoder.recipic.domain.brand.repository;

import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    // BaseIngredient로 연결된 Ingredient 목록 조회
    List<Ingredient> findByBaseIngredient_BaseIngredientId(Integer baseIngredientId);
}
