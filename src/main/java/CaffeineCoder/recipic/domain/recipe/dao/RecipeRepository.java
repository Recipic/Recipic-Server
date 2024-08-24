package CaffeineCoder.recipic.domain.recipe.dao;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
