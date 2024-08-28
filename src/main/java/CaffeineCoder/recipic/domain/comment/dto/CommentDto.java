package CaffeineCoder.recipic.domain.comment.dto;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.user.domain.User;

public record CommentDto(
        Integer commentId,
        String content,
        String createdAt,
        Long userId,
        String userProfileImageUrl,
        String userNickName,
        String recipeTitle,
        Integer recipeId,
        int likeCount,
        boolean isLiked
) {
    public static CommentDto fromEntity(Comment comment, User user, boolean isLiked, int likeCount) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                user.getUserId(),
                user.getProfileImageUrl(),
                user.getNickName(),
                comment.getRecipe().getTitle(), 
                comment.getRecipeId(),
                likeCount,
                isLiked
        );
    }
}
