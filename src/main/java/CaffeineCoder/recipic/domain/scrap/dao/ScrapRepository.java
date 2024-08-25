package CaffeineCoder.recipic.domain.scrap.dao;

import CaffeineCoder.recipic.domain.scrap.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
    Optional<Scrap> findByUserIdAndRecipeId(Long userId, Integer recipeId);
    void deleteByUserIdAndRecipeId(Long userId, Integer recipeId);
}