package CaffeineCoder.recipic.domain.comment.api;

import CaffeineCoder.recipic.domain.comment.dao.CommentLikeRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.comment.domain.CommentLike;
import CaffeineCoder.recipic.domain.comment.dto.CommentRequestDto;
import CaffeineCoder.recipic.domain.comment.dto.CommentResponseDto;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public boolean createComment(CommentRequestDto commentRequestDto) {
        // JWT 토큰에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 유저와 레시피가 존재하는지 확인
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Invalid userId");
        }
        if (!recipeRepository.existsById(commentRequestDto.getRecipeId())) {
            throw new IllegalArgumentException("Invalid recipeId");
        }

        // 댓글 생성 및 저장
        Comment comment = Comment.builder()
                .userId(userId)
                .recipeId(commentRequestDto.getRecipeId())
                .content(commentRequestDto.getComment())
                .build();

        commentRepository.save(comment);
        return true;
    }

    @Transactional
    public boolean toggleLikeComment(Integer commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 댓글이 존재하는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 현재 사용자가 자신이 작성한 댓글인지 확인
        if (comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You cannot like your own comment");
        }

        // 이미 좋아요를 눌렀는지 확인
        Optional<CommentLike> existingLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);

        if (existingLike.isPresent()) {
            // 좋아요를 이미 눌렀다면 좋아요를 취소
            commentLikeRepository.delete(existingLike.get());
            return false; // 좋아요 취소를 나타내기 위해 false 반환
        } else {
            // 좋아요를 누르지 않았다면 좋아요를 추가
            CommentLike commentLike = CommentLike.builder()
                    .userId(userId)
                    .commentId(commentId)
                    .recipeId(comment.getRecipeId())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            commentLikeRepository.save(commentLike);
            return true; // 좋아요 추가를 나타내기 위해 true 반환
        }
    }


    // 댓글 삭제 메소드 추가
    @Transactional
    public boolean deleteComment(Integer commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 해당 ID의 댓글을 찾기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 댓글 작성자와 현재 사용자가 같은지 확인
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this comment");
        }

        // 댓글 삭제
        commentRepository.deleteById(commentId);
        return true;
    }

    //내가 작성한 댓글 목록 메소드 추가
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getUserComments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 사용자가 작성한 댓글 목록을 가져오기
        List<Comment> comments = commentRepository.findByUserId(userId);

        // 댓글 목록을 CommentResponseDto로 변환하여 반환
        return comments.stream().map(comment -> {
            Recipe recipe = recipeRepository.findById(comment.getRecipeId())
                    .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
            int likeCount = commentLikeRepository.countByCommentId(comment.getCommentId());
            return new CommentResponseDto(recipe.getTitle(), comment.getContent(), likeCount);
        }).collect(Collectors.toList());
    }

}
