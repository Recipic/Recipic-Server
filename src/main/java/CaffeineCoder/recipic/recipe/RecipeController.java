package CaffeineCoder.recipic.recipe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerRecipe(@RequestBody Map<String, Object> recipeData) {
        recipeService.registerRecipe(recipeData);

        return ResponseEntity.ok(Map.of("isSuccess", true));
    }
}

