package CaffeineCoder.recipic.domain.announcement.dao;

import CaffeineCoder.recipic.domain.announcement.domain.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

}
