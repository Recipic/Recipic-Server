package CaffeineCoder.recipic.recipe.dao;

import CaffeineCoder.recipic.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.recipe.domain.RecipeIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
}

