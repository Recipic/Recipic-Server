package CaffeineCoder.recipic.recipe.dao;

import CaffeineCoder.recipic.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}