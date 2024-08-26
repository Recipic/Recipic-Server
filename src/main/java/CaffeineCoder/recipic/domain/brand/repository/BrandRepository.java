package CaffeineCoder.recipic.domain.brand.repository;

import CaffeineCoder.recipic.domain.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandName(String brandName);

    @Query("SELECT b.brandId FROM Brand b WHERE b.brandName = :name")
    Optional<Integer> findBrandIdByBrandName(@Param("name") String name);
}
