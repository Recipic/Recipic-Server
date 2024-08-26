package CaffeineCoder.recipic.domain.comment.dao;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT COUNT(*) FROM Comment WHERE recipeId = :recipeId")
    Integer countByRecipeId(@Param("recipeId") Integer recipeId);

    void deleteByRecipeId(Integer recipeId);


    // 특정 사용자가 작성한 댓글을 페이지네이션하여 가져오기
    Page<Comment> findByUserId(Long userId, Pageable pageable);

    // 특정 사용자가 작성한 댓글 중 특정 키워드를 포함한 댓글을 페이지네이션하여 가져오기
    Page<Comment> findByUserIdAndContentContaining(Long userId, String content, Pageable pageable);
}


