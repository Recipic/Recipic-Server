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

    @Column(name = "ingredient_id")
    private Integer ingredientId;

    @Column(name = "recipe_id")
    private Integer recipeId;

    public RecipeIngredientId(Integer ingredientId, Integer recipeId) {
        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return Objects.equals(ingredientId, that.ingredientId) &&
                Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, recipeId);
    }
}