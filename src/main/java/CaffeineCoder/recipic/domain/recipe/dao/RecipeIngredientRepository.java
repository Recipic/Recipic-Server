package CaffeineCoder.recipic.domain.recipe.dao;

import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
}