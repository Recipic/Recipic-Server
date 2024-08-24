package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId; //Long에서 Integer로 수정

    @Column(name = "brand_name")
    private String brandName;

    @OneToMany(mappedBy = "brand")
    private List<BrandIngredient> brandIngredients;

}
