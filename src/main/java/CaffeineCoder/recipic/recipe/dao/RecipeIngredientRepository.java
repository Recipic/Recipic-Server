package CaffeineCoder.recipic.recipe.dao;

import CaffeineCoder.recipic.recipe.domain.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

}