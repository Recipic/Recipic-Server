package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;

@Entity
@Getter
@NoArgsConstructor
public class RecipeBaseIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_baseingredient_id")
    private Integer recipeBaseIngredientId;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "baseingredient_id", nullable = false)
    private BaseIngredient baseIngredient;

    public RecipeBaseIngredient(Recipe recipe, BaseIngredient baseIngredient) {
        this.recipe = recipe;
        this.baseIngredient = baseIngredient;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setBaseIngredient(BaseIngredient baseIngredient) {
        this.baseIngredient = baseIngredient;
    }
}
