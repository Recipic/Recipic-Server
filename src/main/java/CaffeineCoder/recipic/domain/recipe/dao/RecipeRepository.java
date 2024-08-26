package CaffeineCoder.recipic.domain.recipe.dao;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    @Query("SELECT new CaffeineCoder.recipic.domain.recipe.dto.RecipeDto(" +
            "r.recipeId, " +
            "r.userId, " +
            "r.brandId, " +
            "r.title, " +
            "r.description, " +
            "r.imageUrl, " +
            "r.isCelebrity, " +
            "r.createdAt, " +
            "r.status) " +
            "FROM Recipe r " +
            "WHERE r.brandId = :brandId")
    Page<RecipeDto> findRecipesByBrandId(@Param("brandId") Integer brandId, Pageable pageable);
}
