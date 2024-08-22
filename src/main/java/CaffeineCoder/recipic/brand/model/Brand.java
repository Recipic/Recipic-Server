package CaffeineCoder.recipic.brand.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    private String brandName;

    @OneToMany(mappedBy = "brand")
    private List<BrandIngredient> brandIngredients;

    // Getters, setters, constructors...
}

