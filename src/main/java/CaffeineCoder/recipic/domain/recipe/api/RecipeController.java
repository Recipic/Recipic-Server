package CaffeineCoder.recipic.domain.recipe.api;

import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.recipe.application.RecipeService;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeResponseDto;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeRequestDto;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerRecipe(
            @RequestPart(value="recipe") RecipeRequestDto recipeRequestDto,
            @RequestPart(value="thumbnailImage", required = false) MultipartFile thumbnailImage) {
        recipeService.registerRecipe(recipeRequestDto, thumbnailImage);
        return ResponseEntity.ok(Map.of("isSuccess", true));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body("Content-Type is not supported. Please use multipart/form-data.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> deleteRecipe(@RequestParam("recipeId") Integer recipeId) {
        boolean isSuccess = recipeService.deleteRecipe(recipeId);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", isSuccess);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRecipe(
            @RequestPart(value="recipe") RecipeRequestDto recipeRequestDto,
            @RequestPart(value="thumbnailImage") MultipartFile thumbnailImage
    ) {
        boolean isSuccess = recipeService.updateRecipe(recipeRequestDto, thumbnailImage);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", isSuccess);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{recipeId}")
    public ApiResponse<?> getRecipeDetail(@PathVariable("recipeId") Integer recipeId) {
        return ApiUtils.success(recipeService.getRecipeDetail(recipeId));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getRecipeList(
            @RequestParam(value = "keyword", defaultValue = "-1") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            // 전체 레시피 목록 반환
            List<RecipeResponseDto> recipes = recipeService.getQueriedRecipes(keyword, page, size);

            return ResponseEntity.ok(ApiUtils.success(recipes));  // ApiResponse로 반환
        } catch (Exception e) {
            System.out.println("Error occurred while fetching recipe list: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the recipe list");
        }
    }

}
