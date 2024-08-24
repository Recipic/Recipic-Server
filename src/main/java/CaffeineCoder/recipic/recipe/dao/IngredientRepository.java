package CaffeineCoder.recipic.recipe.dao;

import CaffeineCoder.recipic.recipe.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}