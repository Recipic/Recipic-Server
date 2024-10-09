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

    // 유저 조회
    public User findUser(Long userId) {
        // 로그 추가: userId가 올바르게 전달되는지 확인
        System.out.println("Finding user with ID: " + userId);

        // 방어 코드: userId가 null일 경우 예외 처리
        if (userId == null) {
            throw new RuntimeException("User ID is null. Current user is not authenticated.");
        }

        // userId로 User 조회, 없을 경우 예외 발생
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // 레시피 조회
    public Recipe findRecipe(Integer recipeId) {
        // 로그 추가: recipeId가 올바르게 전달되는지 확인
        System.out.println("Finding recipe with ID: " + recipeId);

        // recipeId로 Recipe 조회, 없을 경우 예외 발생
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
    }
}
