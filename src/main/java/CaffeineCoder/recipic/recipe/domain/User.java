package CaffeineCoder.recipic.recipe.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String nickname;
    private Integer oAuthProvider;
}