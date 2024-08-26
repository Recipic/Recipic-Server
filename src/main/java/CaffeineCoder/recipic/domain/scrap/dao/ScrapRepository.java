package CaffeineCoder.recipic.domain.scrap.dao;

import CaffeineCoder.recipic.domain.scrap.domain.Scrap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
    Optional<Scrap> findByUserIdAndRecipeId(Long userId, Integer recipeId);
    void deleteByUserIdAndRecipeId(Long userId, Integer recipeId);

    @Query("SELECT s.recipeId FROM Scrap s JOIN Recipe r ON s.recipeId = r.recipeId WHERE r.isCelebrity = false GROUP BY s.recipeId ORDER BY COUNT(s.recipeId) DESC")
    List<Integer> findTop5NormalRecipesByScrapCount(Pageable pageable);

    @Query("SELECT s.recipeId FROM Scrap s JOIN Recipe r ON s.recipeId = r.recipeId WHERE r.isCelebrity = true GROUP BY s.recipeId ORDER BY COUNT(s.recipeId) DESC")
    List<Integer> findTop5CelebrityRecipesByScrapCount(Pageable pageable);

    int countByRecipeId(Integer recipeId);

    void deleteByRecipeId(Integer recipeId);
}