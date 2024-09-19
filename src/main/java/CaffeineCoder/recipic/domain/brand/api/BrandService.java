package CaffeineCoder.recipic.domain.brand.api;

import CaffeineCoder.recipic.domain.brand.domain.BaseIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Brand;
import CaffeineCoder.recipic.domain.brand.domain.BrandIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import CaffeineCoder.recipic.domain.brand.repository.BaseIngredientRepository;
import CaffeineCoder.recipic.domain.brand.repository.BrandIngredientRepository;
import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import CaffeineCoder.recipic.domain.brand.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandIngredientRepository brandIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final BaseIngredientRepository baseIngredientRepository;

    public BrandService(BrandRepository brandRepository, BrandIngredientRepository brandIngredientRepository,
                        IngredientRepository ingredientRepository, BaseIngredientRepository baseIngredientRepository) {
        this.brandRepository = brandRepository;
        this.brandIngredientRepository = brandIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.baseIngredientRepository = baseIngredientRepository;
    }

    // BaseIngredient 추가 (brandId 사용)
    public boolean addBaseIngredientToBrand(Integer brandId, String ingredientName, Long quantity, String unit, Integer cost, Double calorie) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        if (optionalBrand.isEmpty()) {
            throw new RuntimeException("Brand not found with ID: " + brandId);
        }
        Brand brand = optionalBrand.get();

        // BaseIngredient 생성 및 저장
        BaseIngredient baseIngredient = new BaseIngredient(ingredientName, quantity, unit, cost, calorie);
        baseIngredient.setBrand(brand);  // Brand와 연결
        baseIngredientRepository.save(baseIngredient);  // 저장

        return true;
    }

    // Ingredient 추가 (BaseIngredient와 관계)
    public boolean addIngredient(String ingredientName, Long quantity, String unit, Integer cost, Double calorie) {
        // BaseIngredient를 찾음
        Optional<BaseIngredient> optionalBaseIngredient = baseIngredientRepository.findByIngredientName(ingredientName);
        if (optionalBaseIngredient.isEmpty()) {
            return false;
        }

        // Ingredient 생성 및 저장
        Ingredient ingredient = Ingredient.builder()
                .ingredientName(ingredientName)
                .quantity(quantity)
                .unit(unit)
                .cost(cost)
                .calorie(calorie)
                .baseIngredient(optionalBaseIngredient.get())
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
                .map(baseIngredient -> {
                    Map<String, Object> baseIngredientMap = new LinkedHashMap<>();
                    baseIngredientMap.put("ingredientId", baseIngredient.getBaseIngredientId());
                    baseIngredientMap.put("name", baseIngredient.getIngredientName());
                    baseIngredientMap.put("quantity", baseIngredient.getQuantity());
                    baseIngredientMap.put("unit", baseIngredient.getUnit());
                    baseIngredientMap.put("cost", baseIngredient.getCost());
                    baseIngredientMap.put("calorie", baseIngredient.getCalorie());
                    return baseIngredientMap;
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
