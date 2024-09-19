package CaffeineCoder.recipic.domain.comment.dto;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.user.domain.User;

public class CommentDto {

    private final String commentId;
    private final String content;
    private final String createdAt;
    private final Long userId;
    private final String userProfileImageUrl;
    private final String userNickName;
    private final boolean isLiked;
    private final int likeCount;
    private final boolean isMyComment;  // 본인이 작성한 댓글 여부

    public CommentDto(
            String commentId,
            String content,
            String createdAt,
            Long userId,
            String userProfileImageUrl,
            String userNickName,
            boolean isLiked,
            int likeCount,
            boolean isMyComment
    ) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userProfileImageUrl = userProfileImageUrl;
        this.userNickName = userNickName;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.isMyComment = isMyComment;
    }

    public String getCommentId() { return commentId; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public Long getUserId() { return userId; }
    public String getUserProfileImageUrl() { return userProfileImageUrl; }
    public String getUserNickName() { return userNickName; }
    public boolean isLiked() { return isLiked; }
    public int getLikeCount() { return likeCount; }
    public boolean isMyComment() { return isMyComment; }

    public static CommentDto fromEntity(Comment comment, User user, boolean isLiked, int likeCount, boolean isMyComment) {
        return new CommentDto(
                comment.getCommentId().toString(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                user.getUserId(),
                user.getProfileImageUrl(),
                user.getNickName(),
                isLiked,
                likeCount,
                isMyComment
        );
    }
}
