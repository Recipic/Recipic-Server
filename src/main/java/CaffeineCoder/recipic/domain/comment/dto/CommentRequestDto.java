package CaffeineCoder.recipic.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private Long userId;
    private Integer recipeId;
    private String comment;

    public CommentRequestDto(Long userId, Integer recipeId, String comment) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.comment = comment;
    }
}