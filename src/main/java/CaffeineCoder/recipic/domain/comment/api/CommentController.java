package CaffeineCoder.recipic.domain.comment.api;

import CaffeineCoder.recipic.domain.comment.dto.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/recipe/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 기존 댓글 생성 메소드
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        boolean isSuccess = commentService.createComment(commentRequestDto);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", isSuccess);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> toggleLikeComment(@RequestBody Map<String, Integer> request) {
        Integer commentId = request.get("commentId");
        boolean isLiked = commentService.toggleLikeComment(commentId);

        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제 메소드 추가
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteComment(@RequestBody Map<String, Integer> request) {
        Integer commentId = request.get("commentId");
        boolean isSuccess = commentService.deleteComment(commentId);

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", isSuccess);
        return ResponseEntity.ok(response);
    }
}
