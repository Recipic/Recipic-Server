package CaffeineCoder.recipic.domain.recipe.dao;

import CaffeineCoder.recipic.domain.brand.domain.RecipeBaseIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeBaseIngredientRepository extends JpaRepository<RecipeBaseIngredient, Integer> {
}
