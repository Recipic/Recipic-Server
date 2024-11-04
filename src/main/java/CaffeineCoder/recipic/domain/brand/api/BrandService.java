package CaffeineCoder.recipic.domain.brand.api;

import CaffeineCoder.recipic.domain.brand.domain.*;
import CaffeineCoder.recipic.domain.brand.repository.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BaseIngredientRepository baseIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final BaseIngredientSizeRepository baseIngredientSizeRepository;

    public BrandService(BrandRepository brandRepository, BaseIngredientRepository baseIngredientRepository,
                        IngredientRepository ingredientRepository, BaseIngredientSizeRepository baseIngredientSizeRepository) {
        this.brandRepository = brandRepository;
        this.baseIngredientRepository = baseIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.baseIngredientSizeRepository = baseIngredientSizeRepository;
    }

    // BaseIngredient 추가 (brandId 사용)
    public boolean addBaseIngredientToBrand(Integer brandId, String ingredientName, String size) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + brandId));

        // BaseIngredient 생성 및 저장
        BaseIngredient baseIngredient = new BaseIngredient(ingredientName, brand);
        baseIngredientRepository.save(baseIngredient);

        // BaseIngredientSize도 생성 및 저장
        BaseIngredientSize baseIngredientSize = new BaseIngredientSize(size, baseIngredient);
        baseIngredientSizeRepository.save(baseIngredientSize);

        return true;
    }

    // Ingredient 추가 (BaseIngredient와 연결)
    public boolean addIngredient(Integer baseIngredientId, String ingredientName, Long quantity, String unit, Integer cost, Double calorie) {
        BaseIngredient baseIngredient = baseIngredientRepository.findById(baseIngredientId)
                .orElseThrow(() -> new RuntimeException("BaseIngredient not found with ID: " + baseIngredientId));

        Ingredient ingredient = Ingredient.builder()
                .ingredientName(ingredientName)
                .quantity(quantity)
                .unit(unit)
                .cost(cost)
                .calorie(calorie)
                .baseIngredient(baseIngredient)
                .build();

        ingredientRepository.save(ingredient);
        return true;
    }

    // 브랜드 이름으로 BaseIngredient 가져오기
    public List<Map<String, Object>> getBaseIngredientsByBrandName(String brandName) {
        Brand brand = brandRepository.findByBrandName(brandName)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        return baseIngredientRepository.findByBrand(brand)
                .stream()
                .flatMap(baseIngredient ->
                        baseIngredient.getBaseIngredientSizes().stream().map(size -> {
                            Map<String, Object> baseIngredientMap = new LinkedHashMap<>();
                            baseIngredientMap.put("ingredientId", baseIngredient.getBaseIngredientId());
                            baseIngredientMap.put("ingredientName", baseIngredient.getIngredientName());
                            baseIngredientMap.put("size", size.getSize());
                            return baseIngredientMap;
                        })
                )
                .collect(Collectors.toList());
    }

    // BaseIngredient에 매핑된 Ingredient 조회
    public List<Map<String, Object>> getIngredientsByBaseIngredientId(Integer baseIngredientId) {
        BaseIngredient baseIngredient = baseIngredientRepository.findById(baseIngredientId)
                .orElseThrow(() -> new RuntimeException("BaseIngredient not found"));

        return baseIngredient.getIngredients().stream()
                .map(ingredient -> {
                    Map<String, Object> ingredientMap = new LinkedHashMap<>();
                    ingredientMap.put("ingredientId", ingredient.getIngredientId());
                    ingredientMap.put("ingredientName", ingredient.getIngredientName());
                    ingredientMap.put("quantity", ingredient.getQuantity());
                    ingredientMap.put("unit", ingredient.getUnit());
                    ingredientMap.put("cost", ingredient.getCost());
                    ingredientMap.put("calorie", ingredient.getCalorie());
                    return ingredientMap;
                })
                .collect(Collectors.toList());
    }

    // 모든 브랜드 가져오기
    public List<Map<String, Object>> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brand -> {
                    Map<String, Object> brandMap = new LinkedHashMap<>();
                    brandMap.put("brandId", brand.getBrandId());
                    brandMap.put("brandName", brand.getBrandName());
                    return brandMap;
                })
                .collect(Collectors.toList());
    }
}
