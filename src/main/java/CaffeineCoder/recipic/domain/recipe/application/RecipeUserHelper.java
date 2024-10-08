package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class RecipeUserHelper {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    // 생성자는 @RequiredArgsConstructor에 의해 자동으로 생성됨
    // public RecipeUserHelper(UserRepository userRepository, RecipeRepository recipeRepository) {
    //     this.userRepository = userRepository;
    //     this.recipeRepository = recipeRepository;
    // }

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Recipe findRecipe(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }
}
