package CaffeineCoder.recipic.domain.recipe.domain;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.scrap.domain.Scrap;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Integer recipeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "title")
    private String title;

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

    // 연관 관계 매핑
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<RecipeIngredient> recipeIngredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Scrap> scraps;

    @Builder
    public Recipe(Long userId, Integer brandId, String title, String description, String imageUrl, Boolean isCelebrity, Timestamp createdAt, Integer status) {
        this.userId = userId;
        this.brandId = brandId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isCelebrity = isCelebrity;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());
        this.status = status;
    }

    // 게시글 수정 메소드
    public void updateRecipe(String title, String description, String imageUrl, Integer brandId, Boolean isCelebrity) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.brandId = brandId;
        this.isCelebrity = isCelebrity;
    }
}
