package CaffeineCoder.recipic.domain.scrap.domain;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Integer scrapId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recipe_id", nullable = false)
    private Integer recipeId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Builder
    public Scrap(Long userId, Integer recipeId, Timestamp createdAt) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());
    }

    @ManyToOne
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    private Recipe recipe;
}
