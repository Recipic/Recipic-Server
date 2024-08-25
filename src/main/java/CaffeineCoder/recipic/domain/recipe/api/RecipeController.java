package CaffeineCoder.recipic.domain.recipe.api;

import CaffeineCoder.recipic.domain.recipe.application.RecipeService;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/detail/{recipeId}")
    public ApiResponse<?> getRecipeDetail(@PathVariable("recipeId") Integer recipeId) {
        return ApiUtils.success(recipeService.getRecipeDetail(recipeId));
    }

}