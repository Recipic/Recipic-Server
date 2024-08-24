package CaffeineCoder.recipic.recipe.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Integer count;

    // 생성자 추가
    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Integer count) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.count = count;
    }

    // Getter 메서드만 추가
    public Long getId() {
        return id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Integer getCount() {
        return count;
    }
}
