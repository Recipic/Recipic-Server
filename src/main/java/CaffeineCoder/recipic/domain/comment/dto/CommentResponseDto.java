package CaffeineCoder.recipic.domain.comment.dto;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Integer commentId;
    private final String recipeTitle;
    private final String content;
    private final int likeCount;

    public CommentResponseDto(Comment comment, String recipeTitle, int likeCount) {
        this.commentId = comment.getCommentId();
        this.recipeTitle = recipeTitle;
        this.content = comment.getContent();
        this.likeCount = likeCount;
    }
}