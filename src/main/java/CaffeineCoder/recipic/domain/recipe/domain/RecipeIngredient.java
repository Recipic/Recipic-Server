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

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @MapsId("baseIngredientId")
    @JoinColumn(name = "baseingredient_id")
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
