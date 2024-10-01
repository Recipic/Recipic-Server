package CaffeineCoder.recipic.domain.notification.api;

import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.notification.application.NotificationService;
import CaffeineCoder.recipic.domain.notification.dto.NotificationResponseDto;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/notifications")
public class NotificationApi {

    private final NotificationService notificationService;

    @GetMapping("")
    public ApiResponse<List<NotificationResponseDto>> getUserNotifications() {
        Long userId = SecurityUtil.getCurrentMemberId();
        List<NotificationResponseDto> notifications = notificationService.getUserNotifications(userId)
                .stream()
                .map(NotificationResponseDto::of)
                .collect(Collectors.toList());
        return ApiUtils.success(notifications);
    }

    @PostMapping("/{notificationId}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId) {
        // 알림 읽음 처리
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(Collections.singletonMap("isSuccess", true));
    }

}
