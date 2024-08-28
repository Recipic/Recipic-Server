package CaffeineCoder.recipic.domain.brand.api;

import CaffeineCoder.recipic.domain.brand.dto.IngredientDTO;
import CaffeineCoder.recipic.domain.brand.domain.Brand;
import CaffeineCoder.recipic.domain.brand.domain.BrandIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
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

    public BrandService(BrandRepository brandRepository, BrandIngredientRepository brandIngredientRepository, IngredientRepository ingredientRepository) {
        this.brandRepository = brandRepository;
        this.brandIngredientRepository = brandIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Map<String, Object>> getIngredientsByBrandName(String brandName) {
        Brand brand = brandRepository.findByBrandName(brandName)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        return brandIngredientRepository.findByBrand(brand)
                .stream()
                .map(brandIngredient -> {
                    Ingredient ingredient = brandIngredient.getIngredient();

                    IngredientDTO dto = new IngredientDTO.Builder()
                            .ingredientId(ingredient.getIngredientId())
                            .name(ingredient.getIngredientName())
                            .quantity(ingredient.getQuantity())
                            .unit(ingredient.getUnit())
                            .cost(ingredient.getCost())
                            .calorie(ingredient.getCalorie())
                            .build();

                    return dto.toMap();
                })
                .collect(Collectors.toList());
    }

    public boolean addIngredientToBrand(String brandName, String ingredientName, Long quantity, String unit, Integer cost, Double calorie) {

        Optional<Brand> optionalBrand = brandRepository.findByBrandName(brandName);
        if (optionalBrand.isEmpty()) {
            return false;
        }
        Brand brand = optionalBrand.get();

        // 새로운 Ingredient 객체 생성

        Ingredient ingredient = Ingredient.builder()
                .ingredientName(ingredientName)
                .quantity(quantity)
                .unit(unit)
                .cost(cost)
                .calorie(calorie)
                .build();

        // Ingredient 저장
        ingredientRepository.save(ingredient);

        // BrandIngredient 객체 생성
        BrandIngredient brandIngredient = new BrandIngredient();
        brandIngredient.setIngredient(ingredient);
        brandIngredient.setBrand(brand);
        brandIngredientRepository.save(brandIngredient);

        return true;
    }

    //모든 브랜드 가져오기
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
