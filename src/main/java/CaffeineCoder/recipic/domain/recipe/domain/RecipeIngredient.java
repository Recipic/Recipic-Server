package CaffeineCoder.recipic.domain.recipe.domain;

import CaffeineCoder.recipic.domain.brand.domain.BaseIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    // 사이드 재료 매핑
    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id", insertable = false, updatable = false)
    private Ingredient ingredient;

    // 베이스 재료 매핑
    @ManyToOne
    @MapsId("baseIngredientId")
    @JoinColumn(name = "baseingredient_id", referencedColumnName = "baseingredient_id", insertable = false, updatable = false)
    private BaseIngredient baseIngredient;

    @Column(name = "count")
    private Integer count;

    @Builder
    public RecipeIngredient(RecipeIngredientId id, Recipe recipe, Ingredient ingredient, BaseIngredient baseIngredient, Integer count) {
        this.id = id;
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.baseIngredient = baseIngredient;
        this.count = count;
    }
}
