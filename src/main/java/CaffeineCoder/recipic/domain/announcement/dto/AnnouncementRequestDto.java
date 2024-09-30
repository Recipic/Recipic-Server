package CaffeineCoder.recipic.domain.announcement.dto;


import CaffeineCoder.recipic.domain.announcement.domain.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementRequestDto {
    private String title;
    private String description;
}
