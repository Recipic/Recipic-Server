package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeUserHelper {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    // 유저 조회
    public User findUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User ID is null. The user is not authenticated.");
        }
        System.out.println("Current User ID: " + userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // 레시피 조회
    public Recipe findRecipe(Integer recipeId) {
        // recipeId로 Recipe 조회, 없을 경우 예외 발생
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
    }
}
