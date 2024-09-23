package CaffeineCoder.recipic.domain.brand.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BaseIngredientSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "baseingredient_size_id")
    private Integer baseIngredientSizeId;

    @Column(name = "size", nullable = false)
    private String size;

    @ManyToOne
    @JoinColumn(name = "baseingredient_id", nullable = false)
    private BaseIngredient baseIngredient;

    public BaseIngredientSize(String size, BaseIngredient baseIngredient) {
        this.size = size;
        this.baseIngredient = baseIngredient;
    }
}


