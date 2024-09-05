package CaffeineCoder.recipic.domain.comment.dao;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // 특정 레시피의 댓글을 최신순으로 조회 (페이지네이션 지원)
    @Query("SELECT c FROM Comment c WHERE c.recipeId = :recipeId ORDER BY c.createdAt DESC")
    Page<Comment> findByRecipeIdOrderByCreatedAtDesc(@Param("recipeId") Integer recipeId, Pageable pageable);

    // 특정 레시피의 댓글을 좋아요 순으로 조회 (페이지네이션 지원)
    @Query("SELECT c FROM Comment c LEFT JOIN CommentLike cl ON c.commentId = cl.commentId WHERE c.recipeId = :recipeId GROUP BY c.commentId ORDER BY COUNT(cl.commentId) DESC, c.createdAt DESC")
    Page<Comment> findByRecipeIdOrderByLikes(@Param("recipeId") Integer recipeId, Pageable pageable);

    // 내가 작성한 댓글을 최신순으로 조회 (페이지네이션 없음)
    @Query("SELECT c FROM Comment c WHERE c.userId = :userId ORDER BY c.createdAt DESC")
    List<Comment> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    // 내가 작성한 댓글을 좋아요 순으로 조회 (페이지네이션 없음)
    @Query("SELECT c FROM Comment c LEFT JOIN CommentLike cl ON c.commentId = cl.commentId WHERE c.userId = :userId GROUP BY c.commentId ORDER BY COUNT(cl.commentId) DESC, c.createdAt DESC")
    List<Comment> findByUserIdOrderByLikes(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.recipeId = :recipeId")
    Integer countByRecipeId(@Param("recipeId") Integer recipeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.recipeId = :recipeId")
    void deleteByRecipeId(@Param("recipeId") Integer recipeId);
}
