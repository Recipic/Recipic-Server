package CaffeineCoder.recipic.domain.user.api;

import CaffeineCoder.recipic.domain.comment.api.CommentService;
import CaffeineCoder.recipic.domain.comment.dto.CommentResponseDto;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/comments")
public class UserCommentApi {

    private final CommentService commentService;

    @GetMapping("")
    public ApiResponse<List<CommentResponseDto>> getUserComments(
            @RequestParam(value = "sortType", defaultValue = "latest") String sortType) {
        Long userId = SecurityUtil.getCurrentMemberId();
        List<CommentResponseDto> userComments = commentService.getUserComments(userId, sortType);
        return ApiUtils.success(userComments);
    }
}
