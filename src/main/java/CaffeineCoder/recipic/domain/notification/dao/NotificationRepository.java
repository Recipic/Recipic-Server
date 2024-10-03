package CaffeineCoder.recipic.domain.notification.dao;

import CaffeineCoder.recipic.domain.notification.domain.Notification;
import CaffeineCoder.recipic.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByNotificationIdDesc(User user);
}
