package CaffeineCoder.recipic.domain.brand.repository;

import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    @Query("SELECT i.ingredientId FROM Ingredient i WHERE i.ingredientName = :name")
    Optional<Integer> findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecipeIngredient ri WHERE ri.recipe.recipeId = :recipeId")
    void deleteByRecipeId(@Param("recipeId") Integer recipeId);

}
