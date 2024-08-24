package CaffeineCoder.recipic.domain.recipe.api;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/register")
    public ResponseEntity<?> registerRecipe(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = recipeService.registerRecipe(recipeDto);
        return ResponseEntity.ok(new ApiResponse(true, "Recipe registered successfully"));
    }
}
