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

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "recipe_id")
    private Integer recipeId;

    public RecipeIngredientId(String ingredientName, Integer recipeId) {
        this.ingredientName = ingredientName;
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return Objects.equals(ingredientName, that.ingredientName) &&
                Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientName, recipeId);
    }
}
