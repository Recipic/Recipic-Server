package CaffeineCoder.recipic.domain.brand.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // BaseIngredient 추가 API
    @PostMapping("/add-baseingredient")
    public ResponseEntity<Map<String, Object>> addBaseIngredientToBrand(@RequestBody Map<String, Object> request) {
        String brandName = (String) request.get("brandName");
        String ingredientName = (String) request.get("ingredientName");
        Long quantity = request.get("quantity") instanceof Integer
                ? Long.valueOf((Integer) request.get("quantity"))
                : (Long) request.get("quantity");
        String unit = (String) request.get("unit");
        Integer cost = (Integer) request.get("cost");
        Double calorie = request.get("calorie") instanceof Integer
                ? Double.valueOf((Integer) request.get("calorie"))
                : (Double) request.get("calorie");

        boolean success = brandService.addBaseIngredientToBrand(brandName, ingredientName, quantity, unit, cost, calorie);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", success);
        response.put("message", success ? "Base Ingredient added successfully" : "Failed to add Base Ingredient");

        return ResponseEntity.ok(response);
    }

    // Ingredient 추가 API
    @PostMapping("/add-ingredient")
    public ResponseEntity<Map<String, Object>> addIngredient(@RequestBody Map<String, Object> request) {
        String ingredientName = (String) request.get("ingredientName");
        Long quantity = request.get("quantity") instanceof Integer
                ? Long.valueOf((Integer) request.get("quantity"))
                : (Long) request.get("quantity");
        String unit = (String) request.get("unit");
        Integer cost = (Integer) request.get("cost");
        Double calorie = request.get("calorie") instanceof Integer
                ? Double.valueOf((Integer) request.get("calorie"))
                : (Double) request.get("calorie");

        boolean success = brandService.addIngredient(ingredientName, quantity, unit, cost, calorie);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", success);
        response.put("message", success ? "Ingredient added successfully" : "Failed to add Ingredient");

        return ResponseEntity.ok(response);
    }

    // 브랜드 이름으로 BaseIngredient 조회 API
    @GetMapping("/baseingredients")
    public ResponseEntity<Map<String, Object>> getBaseIngredientsByBrandName(@RequestParam("brandName") String brandName) {
        List<Map<String, Object>> baseIngredients = brandService.getBaseIngredientsByBrandName(brandName);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", true);
        response.put("response", baseIngredients);

        return ResponseEntity.ok(response);
    }

    // 모든 브랜드 가져오기
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllBrands() {
        List<Map<String, Object>> brands = brandService.getAllBrands();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", true);
        response.put("response", brands);

        return ResponseEntity.ok(response);
    }
}
