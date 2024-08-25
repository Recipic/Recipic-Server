package CaffeineCoder.recipic.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeRequestDto {

    private Integer commentId;

    public CommentLikeRequestDto(Long userId, Integer commentId) {

        this.commentId = commentId;
    }
}
