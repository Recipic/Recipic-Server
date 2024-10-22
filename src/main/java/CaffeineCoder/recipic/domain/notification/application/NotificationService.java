package CaffeineCoder.recipic.domain.notification.application;

import CaffeineCoder.recipic.domain.notification.dao.NotificationRepository;
import CaffeineCoder.recipic.domain.notification.domain.Notification;
import CaffeineCoder.recipic.domain.notification.domain.ResourceNotFoundException;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.global.error.exception.InvalidUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // 알림 생성
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNotification(String title, String description, Long recipeId, Long userId) {
        try {
            // 유저 정보 가져오기
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new InvalidUserException());

            // 알림 생성
            Notification notification = Notification.builder()
                    .title(title)
                    .description(description)
                    .isChecked(false) // 읽음 상태 초기값 false
                    .recipeId(recipeId) // 관련된 recipeId
                    .user(user) // 알림을 받을 사용자
                    .build();

            // 알림 저장
            notificationRepository.save(notification);
        } catch (Exception e) {
            // 예외 발생 시 출력
            e.printStackTrace();
        }
    }

    // 사용자 알림 조회
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserException());
        return notificationRepository.findByUserOrderByNotificationIdDesc(user);
    }

    // 알림 읽음 처리
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.markAsRead();
    }

    public void deleteNotificationsByUserId(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }
}
