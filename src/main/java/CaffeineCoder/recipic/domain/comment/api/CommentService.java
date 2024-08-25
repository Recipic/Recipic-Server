package CaffeineCoder.recipic.domain.comment.api;

import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.comment.dto.CommentRequestDto;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public boolean createComment(CommentRequestDto commentRequestDto) {
        // 유저와 레시피가 존재하는지 확인
        if (!userRepository.existsById(commentRequestDto.getUserId())) {
            throw new IllegalArgumentException("Invalid userId");
        }
        if (!recipeRepository.existsById(commentRequestDto.getRecipeId())) {
            throw new IllegalArgumentException("Invalid recipeId");
        }

        // 댓글 생성 및 저장
        Comment comment = Comment.builder()
                .userId(commentRequestDto.getUserId())
                .recipeId(commentRequestDto.getRecipeId())
                .content(commentRequestDto.getComment())
                .build();

        commentRepository.save(comment);
        return true;
    }
}
