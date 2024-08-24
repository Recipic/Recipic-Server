package CaffeineCoder.recipic.domain.brand.repository;

import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
