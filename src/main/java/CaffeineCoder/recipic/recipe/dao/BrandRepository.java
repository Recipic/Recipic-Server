package CaffeineCoder.recipic.recipe.dao;

import CaffeineCoder.recipic.recipe.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BrandRepository extends JpaRepository<Brand, Long> {

}