package CaffeineCoder.recipic.recipe.dao;

import CaffeineCoder.recipic.recipe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}