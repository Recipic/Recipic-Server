package CaffeineCoder.recipic.domain.recipe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
public class RecipeIngredientId implements Serializable {

    @Column(name = "recipe_id", nullable = false)
    private Integer recipeId;

    @Column(name = "ingredient_id", nullable = false)
    private Integer ingredientId;

    @Column(name = "baseingredient_id", nullable = false)
    private Integer baseIngredientId;

    public RecipeIngredientId(Integer recipeId, Integer ingredientId, Integer baseIngredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.baseIngredientId = baseIngredientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return Objects.equals(recipeId, that.recipeId) &&
                Objects.equals(ingredientId, that.ingredientId) &&
                Objects.equals(baseIngredientId, that.baseIngredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId, baseIngredientId);
    }
}
