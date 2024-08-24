package CaffeineCoder.recipic.domain.recipe.dao;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}