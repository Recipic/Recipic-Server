package CaffeineCoder.recipic.domain.comment.dao;

import CaffeineCoder.recipic.domain.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Integer commentId);

    void deleteByUserIdAndCommentId(Long userId, Integer commentId);

    // 댓글 ID로 좋아요 수를 세는 메서드
    int countByCommentId(Integer commentId);
}
