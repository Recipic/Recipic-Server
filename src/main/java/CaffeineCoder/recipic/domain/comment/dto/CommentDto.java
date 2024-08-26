package CaffeineCoder.recipic.domain.comment.dto;

import CaffeineCoder.recipic.domain.comment.domain.Comment;

public record CommentDto(
        String commentId,
        String userId,
        String content,
        String createdAt,
        int likeCount
) {
    public static CommentDto fromEntity(Comment comment, int likeCount) {
        return new CommentDto(
                comment.getCommentId().toString(),
                comment.getUserId().toString(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                likeCount
        );
    }
}
