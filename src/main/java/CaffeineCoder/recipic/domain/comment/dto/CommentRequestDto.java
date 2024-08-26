package CaffeineCoder.recipic.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private Integer recipeId;
    private String comment;

    public CommentRequestDto(Integer recipeId, String comment) {
        this.recipeId = recipeId;
        this.comment = comment;
    }
}
