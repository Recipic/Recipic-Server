package CaffeineCoder.recipic.recipe.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Integer recipeId;

    @Column(name = "user_id")
    private final Long userId;

    @Column(name = "brand_id")
    private final Integer brandId;

    @Column(name = "title")
    private final String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_celebrity")
    private Boolean isCelebrity;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "status")
    private Integer status;

    // 연관 관계 매핑 (optional)
    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeIngredients;

    @Builder
    public Recipe(Long userId, Integer brandId, String title, String description, String imageUrl, Boolean isCelebrity, Timestamp createdAt, Integer status) {
        this.userId = userId;
        this.brandId = brandId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isCelebrity = isCelebrity;
        this.createdAt = createdAt;
        this.status = status;
    }
}

