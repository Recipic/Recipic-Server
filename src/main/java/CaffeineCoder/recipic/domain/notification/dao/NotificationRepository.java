package CaffeineCoder.recipic.domain.notification.dao;

import CaffeineCoder.recipic.domain.notification.domain.Notification;
import CaffeineCoder.recipic.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByNotificationIdDesc(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
