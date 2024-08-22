package CaffeineCoder.recipic.brand.controller;

import CaffeineCoder.recipic.brand.service.BrandService;
import CaffeineCoder.recipic.brand.model.Ingredient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Map<String, Object>> getIngredientsByBrandName(@RequestBody Map<String, String> request) {
        String brandName = request.get("brandName");

        List<Ingredient> ingredients = brandService.getIngredientsByBrandName(brandName);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", true);
        response.put("response", ingredients);

        return ResponseEntity.ok(response);
    }
}
