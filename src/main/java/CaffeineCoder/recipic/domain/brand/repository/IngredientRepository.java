package CaffeineCoder.recipic.domain.brand.repository;

import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    // Ingredient 이름으로 찾기
    @Query("SELECT i FROM Ingredient i WHERE i.ingredientName = :name")
    Optional<Ingredient> findByIngredientName(@Param("name") String name);
}
