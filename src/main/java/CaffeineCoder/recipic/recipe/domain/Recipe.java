package CaffeineCoder.recipic.recipe.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private String title;
    private String description;
    private String imageUrl;
    private Boolean isCelebrity;
    private Timestamp createdAt;
    private Integer status;

    // 생성자 추가
    public Recipe(User user, Brand brand, String title, String description,
                  String imageUrl, Boolean isCelebrity, Integer status) {
        this.user = user;
        this.brand = brand;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isCelebrity = isCelebrity;
        this.status = status;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Getter 메서드만 추가 (Setter는 생략)
    public Long getRecipeId() {
        return recipeId;
    }

    public User getUser() {
        return user;
    }

    public Brand getBrand() {
        return brand;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getIsCelebrity() {
        return isCelebrity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Integer getStatus() {
        return status;
    }
}
