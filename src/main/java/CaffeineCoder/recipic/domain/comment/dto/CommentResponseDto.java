package CaffeineCoder.recipic.domain.comment.dto;

import CaffeineCoder.recipic.domain.comment.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final String recipeTitle;
    private final String content;
    private final int likeCount;

    public CommentResponseDto(String recipeTitle, String content, int likeCount) {
        this.recipeTitle = recipeTitle;
        this.content = content;
        this.likeCount = likeCount;
    }
}