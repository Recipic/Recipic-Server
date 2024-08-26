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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/comments")
public class UserCommentApi {

    private final CommentService commentService;

    @GetMapping("")
    public ApiResponse<Page<CommentResponseDto>> getUserComments(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Long userId = SecurityUtil.getCurrentMemberId();
        Page<CommentResponseDto> userComments = commentService.getUserComments(keyword, page, size, userId);
        return ApiUtils.success(userComments);
    }
}