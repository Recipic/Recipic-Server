package CaffeineCoder.recipic.domain.recipe.domain;

import CaffeineCoder.recipic.domain.brand.domain.Brand;
import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.scrap.domain.Scrap;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_celebrity")
    private Boolean isCelebrity;

    @Column(name = "created_at", updatable = false)
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
    public Recipe(Long userId, Brand brand, String title, String description, String imageUrl, Boolean isCelebrity, Timestamp createdAt, Integer status) {
        this.userId = userId;
        this.brand = brand;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isCelebrity = isCelebrity;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

    // 게시글 수정 메소드
    public void updateRecipe(String title, String description, String imageUrl, Brand brand, Boolean isCelebrity) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.brand = brand;
        this.isCelebrity = isCelebrity;
    }



    // Brand의 이름을 반환하는 메서드
    public String getBrandName() {
        return this.brand != null ? this.brand.getBrandName() : null;
    }
}
