package CaffeineCoder.recipic.domain.brand.api;

import CaffeineCoder.recipic.domain.brand.dto.IngredientDTO;
import CaffeineCoder.recipic.domain.brand.domain.Brand;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import CaffeineCoder.recipic.domain.brand.repository.BrandIngredientRepository;
import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandIngredientRepository brandIngredientRepository;

    public BrandService(BrandRepository brandRepository, BrandIngredientRepository brandIngredientRepository) {
        this.brandRepository = brandRepository;
        this.brandIngredientRepository = brandIngredientRepository;
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

}
