package CaffeineCoder.recipic.domain.recipe.api;

import CaffeineCoder.recipic.domain.recipe.application.RecipeService;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeResponseDto;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeRequestDto;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {

        this.recipeService = recipeService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerRecipe(@RequestBody RecipeRequestDto recipeRequestDto) {
        recipeService.registerRecipe(recipeRequestDto);

        return ResponseEntity.ok(Map.of("isSuccess", true));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> deleteRecipe(@RequestBody Map<String, Integer> request) {
        Integer recipeId = request.get("recipeId");
        boolean isSuccess = recipeService.deleteRecipe(recipeId);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", isSuccess);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRecipe(@RequestBody RecipeRequestDto recipeRequestDto) {
        boolean isSuccess = recipeService.updateRecipe(recipeRequestDto);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", isSuccess);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{recipeId}")
    public ApiResponse<?> getRecipeDetail(@PathVariable("recipeId") Integer recipeId) {
        return ApiUtils.success(recipeService.getRecipeDetail(recipeId));
    }

    @GetMapping("/list")
    public ApiResponse<?> getScraps(
            @RequestParam(value = "keyword", defaultValue = "-1") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiUtils.success(recipeService.getQueriedRecipes(keyword, page, size));
    }
}