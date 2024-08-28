package CaffeineCoder.recipic.domain.comment.dto;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.user.domain.User;

public record CommentDto(
        String commentId,
        String content,
        String createdAt,
        Long userId,
        String userProfileImageUrl,
        String userNickName,
        boolean isLiked,
        int likeCount
) {
    public static CommentDto fromEntity(Comment comment, User user,boolean isLiked, int likeCount) {
        return new CommentDto(
                comment.getCommentId().toString(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                user.getUserId(),
                user.getProfileImageUrl(),
                user.getNickName(),
                isLiked,
                likeCount
        );
    }
}
