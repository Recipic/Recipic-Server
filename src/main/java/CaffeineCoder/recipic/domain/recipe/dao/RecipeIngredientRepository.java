package CaffeineCoder.recipic.domain.recipe.dao;

import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.recipe.recipeId = :recipeId")
    List<RecipeIngredient> findByRecipeId(@Param("recipeId") Integer recipeId);
}