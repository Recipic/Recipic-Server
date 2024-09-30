package CaffeineCoder.recipic.domain.announcement.dto;

import CaffeineCoder.recipic.domain.announcement.domain.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementResponseDto {
    private Long announcementId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Timestamp createdAt;

    public static AnnouncementResponseDto of(Announcement announcement) {
        return new AnnouncementResponseDto(
                announcement.getAnnouncementId(),
                announcement.getTitle(),
                announcement.getDescription(),
                announcement.getThumbnailUrl(),
                announcement.getCreatedAt());
    }
}
