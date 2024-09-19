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

    // BaseIngredient 추가 API (brandId 사용)
    @PostMapping("/add-baseingredient")
    public ResponseEntity<Map<String, Object>> addBaseIngredientToBrand(@RequestBody Map<String, Object> request) {
        Integer brandId = (Integer) request.get("brandId");
        String ingredientName = (String) request.get("ingredientName");
        Long quantity = Long.parseLong(request.get("quantity").toString());
        String unit = (String) request.get("unit");
        Integer cost = (Integer) request.get("cost");
        Double calorie = Double.parseDouble(request.get("calorie").toString());

        boolean success = brandService.addBaseIngredientToBrand(brandId, ingredientName, quantity, unit, cost, calorie);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", success);
        response.put("message", success ? "Base Ingredient added successfully" : "Failed to add Base Ingredient");

        return ResponseEntity.ok(response);
    }

    // Ingredient 추가 API (BaseIngredient에 연결)
    @PostMapping("/add-ingredient")
    public ResponseEntity<Map<String, Object>> addIngredient(@RequestBody Map<String, Object> request) {
        Integer baseIngredientId = (Integer) request.get("baseIngredientId");  // BaseIngredient의 ID를 받음
        String ingredientName = (String) request.get("ingredientName");
        Long quantity = Long.parseLong(request.get("quantity").toString());
        String unit = (String) request.get("unit");
        Integer cost = (Integer) request.get("cost");
        Double calorie = Double.parseDouble(request.get("calorie").toString());

        // baseIngredientId를 추가해서 호출
        boolean success = brandService.addIngredient(baseIngredientId, ingredientName, quantity, unit, cost, calorie);

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

    // 특정 BaseIngredient에 매핑된 Ingredient 조회 API
    @GetMapping("/baseingredient/{baseIngredientId}/ingredients")
    public ResponseEntity<Map<String, Object>> getIngredientsByBaseIngredientId(@PathVariable Integer baseIngredientId) {
        List<Map<String, Object>> ingredients = brandService.getIngredientsByBaseIngredientId(baseIngredientId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", true);
        response.put("response", ingredients);

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
