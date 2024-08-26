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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Column(name = "recipe_id", nullable = false)
    private Integer recipeId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Builder
    public CommentLike(Long userId, Integer commentId, Integer recipeId, Timestamp createdAt) {
        this.userId = userId;
        this.commentId = commentId;
        this.recipeId = recipeId;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());
    }
}

