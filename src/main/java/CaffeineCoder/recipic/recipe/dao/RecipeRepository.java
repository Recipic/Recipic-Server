package CaffeineCoder.recipic.recipe.dao;

import CaffeineCoder.recipic.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
