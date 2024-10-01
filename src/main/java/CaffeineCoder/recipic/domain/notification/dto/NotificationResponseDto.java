package CaffeineCoder.recipic.domain.notification.dto;

import CaffeineCoder.recipic.domain.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
    private Long notificationId;
    private String title;
    private String description;
    private Long recipeId;
    private boolean isChecked;

    public static NotificationResponseDto of(Notification notification) {
        return new NotificationResponseDto(
                notification.getNotificationId(),
                notification.getTitle(),
                notification.getDescription(),
                notification.getRecipeId(),
                notification.isChecked()
        );
    }
}
