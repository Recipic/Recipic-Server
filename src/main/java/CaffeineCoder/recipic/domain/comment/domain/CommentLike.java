package CaffeineCoder.recipic.domain.comment.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer likeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Builder
    public CommentLike(Long userId, Integer commentId) {
        this.userId = userId;
        this.commentId = commentId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
