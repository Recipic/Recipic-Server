package CaffeineCoder.recipic.domain.brand.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // /ingredients 엔드포인트를 GET 방식으로 변경
    @GetMapping("/ingredients")
    public ResponseEntity<Map<String, Object>> getIngredientsByBrandName(@RequestParam("brandName") String brandName) {
        // @RequestBody 대신 @RequestParam을 사용하여 쿼리 매개변수로 brandName을 받음
        List<Map<String, Object>> ingredients = brandService.getIngredientsByBrandName(brandName);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", true);
        response.put("response", ingredients);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-ingredient")
    public ResponseEntity<Map<String, Object>> addIngredientToBrand(@RequestBody Map<String, Object> request) {
        String brandName = (String) request.get("brandName");
        String ingredientName = (String) request.get("ingredientName");

        // quantity는 클라이언트에서 Long 타입으로 보내야함
        Long quantity = request.get("quantity") instanceof Integer
                ? Long.valueOf((Integer) request.get("quantity"))
                : (Long) request.get("quantity");

        String unit = (String) request.get("unit");
        Integer cost = (Integer) request.get("cost");

        // calorie는 Double로 변환
        Double calorie = request.get("calorie") instanceof Integer
                ? Double.valueOf((Integer) request.get("calorie"))
                : (Double) request.get("calorie");

        boolean success = brandService.addIngredientToBrand(brandName, ingredientName, quantity, unit, cost, calorie);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", success);
        if (success) {
            response.put("message", "Ingredient added successfully.");
        } else {
            response.put("message", "Failed to add ingredient.");
        }

        return ResponseEntity.ok(response);
    }

    //모든 브랜드 가져오기
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllBrands() {
        List<Map<String, Object>> brands = brandService.getAllBrands();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("isSuccess", true);
        response.put("response", brands);

        return ResponseEntity.ok(response);
    }
}
