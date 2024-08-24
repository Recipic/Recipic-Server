package CaffeineCoder.recipic.domain.recipe.domain;

import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // 기본 생성자 추가
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "count")
    private Integer count;

    @Builder
    public RecipeIngredient(RecipeIngredientId id, Recipe recipe, Ingredient ingredient, Integer count) {
        this.id = id;
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.count = count;
    }
}