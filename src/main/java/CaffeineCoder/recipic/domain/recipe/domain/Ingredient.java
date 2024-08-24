package CaffeineCoder.recipic.domain.recipe.domain;

import jakarta.persistence.*;

// Ingredient 엔티티에서 ingredient_id의 데이터 타입이 일치해야 합니다.
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;
}
