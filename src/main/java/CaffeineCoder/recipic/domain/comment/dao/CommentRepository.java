package CaffeineCoder.recipic.domain.comment.dao;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT COUNT(*) FROM Comment WHERE recipeId = :recipeId")
    Integer countByRecipeId(@Param("recipeId") Integer recipeId);

    void deleteByRecipeId(Integer recipeId);

}