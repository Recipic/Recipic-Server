package CaffeineCoder.recipic.domain.comment.domain;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "recipe_id")
    private Integer recipeId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Builder
    public Comment(Long userId, Integer recipeId, String content, Timestamp createdAt) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.content = content;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());
    }

    @ManyToOne
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    private Recipe recipe;
}
