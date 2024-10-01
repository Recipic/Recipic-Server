package CaffeineCoder.recipic.domain.announcement.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Entity
@NoArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private Long announcementId;

    @Column(name = "title")
    private String title;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;

    @Builder
    public Announcement(Long announcementId, String title, String thumbnailUrl, String description, Timestamp createdAt) {
        this.announcementId = announcementId;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());;
    }

}
