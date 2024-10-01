package CaffeineCoder.recipic.domain.comment.api;

import CaffeineCoder.recipic.domain.comment.dao.CommentLikeRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.comment.domain.CommentLike;
import CaffeineCoder.recipic.domain.comment.dto.CommentDto;
import CaffeineCoder.recipic.domain.comment.dto.CommentRequestDto;
import CaffeineCoder.recipic.domain.comment.dto.CommentResponseDto;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.notification.application.NotificationService;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public boolean createComment(CommentRequestDto commentRequestDto) {
        try {
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
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            commentRepository.save(comment);

            // 댓글 작성자와 레시피 작성자가 다를 경우 알림 생성
            Recipe recipe = recipeRepository.findById(commentRequestDto.getRecipeId())
                    .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

            if (!recipe.getUserId().equals(userId)) {
                User recipeOwner = userRepository.findById(recipe.getUserId())
                        .orElseThrow(() -> new RuntimeException("Recipe owner not found"));

                String description = "회원님의 게시글에 새로운 댓글이 달렸습니다.";

                // 로깅 추가
                System.out.println("알림 생성: " + description + ", 레시피 ID: " + recipe.getRecipeId() + ", 작성자 ID: " + recipeOwner.getUserId());

                notificationService.createNotification(
                        "게시글 댓글 알림",
                        description,
                        recipe.getRecipeId().longValue(),
                        recipeOwner.getUserId()
                );
            } else {
                // 댓글 작성자가 레시피 작성자인 경우 로깅
                System.out.println("레시피 작성자와 댓글 작성자가 동일하므로 알림이 생성되지 않습니다.");
            }

            return true;
        } catch (Exception e) {
            // 예외 발생 시 출력
            e.printStackTrace();
            return false;
        }
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

        Optional<CommentLike> existingLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);

        if (existingLike.isPresent()) {
            // 좋아요를 이미 눌렀다면 좋아요 취소
            commentLikeRepository.delete(existingLike.get());
            return false;
        } else {
            // 좋아요 추가
            CommentLike commentLike = CommentLike.builder()
                    .userId(userId)
                    .commentId(commentId)
                    .recipeId(comment.getRecipeId())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            commentLikeRepository.save(commentLike);

            // 댓글 작성자에게 좋아요 알림 생성
            User commentOwner = userRepository.findById(comment.getUserId())
                    .orElseThrow(() -> new RuntimeException("Comment owner not found"));

            String description = "회원님의 댓글에 좋아요가 눌렸습니다.";
            notificationService.createNotification(
                    "댓글 좋아요 알림",
                    description,
                    comment.getRecipeId().longValue(),
                    commentOwner.getUserId()
            );
            return true;
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

    // 내가 작성한 댓글 목록
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getUserComments(Long userId, String sortType) {
        List<Comment> comments;

        // 정렬 기준에 따라 댓글을 가져옴
        if ("likes".equalsIgnoreCase(sortType)) {
            comments = commentRepository.findByUserIdOrderByLikes(userId);
        } else {
            comments = commentRepository.findByUserIdOrderByCreatedAtDesc(userId);  // 기본적으로 최신순
        }

        // 댓글을 DTO로 변환하여 리스트로 반환
        return comments.stream()
                .map(comment -> {
                    int likeCount = commentLikeRepository.countByCommentId(comment.getCommentId());
                    return new CommentResponseDto(
                            comment,
                            comment.getRecipe().getTitle(),
                            likeCount
                    );
                })
                .collect(Collectors.toList());
    }

    // 레시피 상세 페이지 댓글 조회
    public Page<CommentDto> getComments(Integer recipeId, int page, int size, String sortType) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage;

        // 정렬 기준에 따라 댓글을 가져옴
        if ("likes".equalsIgnoreCase(sortType)) {
            commentsPage = commentRepository.findByRecipeIdOrderByLikes(recipeId, pageable);
        } else {
            commentsPage = commentRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId, pageable);  // 기본적으로 최신순
        }

        Long currentUserId = SecurityUtil.getCurrentMemberId();  // 현재 사용자 ID 가져오기

        return commentsPage.map(comment -> {
            User user = userRepository.findById(comment.getUserId())
                    .orElseThrow(() -> new RuntimeException("User with id " + comment.getUserId() + " not found"));

            int likeCount = commentLikeRepository.countByCommentId(comment.getCommentId());

            boolean isLiked = commentLikeRepository.findByUserIdAndCommentId(currentUserId, comment.getCommentId()).isPresent();

            boolean isMyComment = comment.getUserId().equals(currentUserId);  // 본인이 작성한 댓글 여부 확인

            return CommentDto.fromEntity(comment, user, isLiked, likeCount, isMyComment);
        });
    }
}
