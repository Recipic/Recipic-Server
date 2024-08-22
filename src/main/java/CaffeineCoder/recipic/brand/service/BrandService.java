package CaffeineCoder.recipic.brand.service;

import CaffeineCoder.recipic.brand.model.Brand;
import CaffeineCoder.recipic.brand.model.BrandIngredient;
import CaffeineCoder.recipic.brand.model.Ingredient;
import CaffeineCoder.recipic.brand.repository.BrandIngredientRepository;
import CaffeineCoder.recipic.brand.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandIngredientRepository brandIngredientRepository;

    public BrandService(BrandRepository brandRepository, BrandIngredientRepository brandIngredientRepository) {
        this.brandRepository = brandRepository;
        this.brandIngredientRepository = brandIngredientRepository;
    }

    public List<Ingredient> getIngredientsByBrandName(String brandName) {
        Brand brand = brandRepository.findByBrandName(brandName)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        return brandIngredientRepository.findByBrand(brand)
                .stream()
                .map(BrandIngredient::getIngredient)
                .collect(Collectors.toList());
    }
}
