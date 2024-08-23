package CaffeineCoder.recipic.brand.service;

import CaffeineCoder.recipic.brand.DTO.IngredientDTO;
import CaffeineCoder.recipic.brand.model.Brand;
import CaffeineCoder.recipic.brand.model.Ingredient;
import CaffeineCoder.recipic.brand.repository.BrandIngredientRepository;
import CaffeineCoder.recipic.brand.repository.BrandRepository;
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
                    IngredientDTO dto = new IngredientDTO(
                            ingredient.getIngredientId(),
                            ingredient.getIngredientName(),
                            ingredient.getQuantity(),
                            ingredient.getUnit(),
                            ingredient.getCost(),
                            ingredient.getCalorie()
                    );
                    return dto.toMap();
                })
                .collect(Collectors.toList());
    }

}
