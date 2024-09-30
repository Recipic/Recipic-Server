package CaffeineCoder.recipic.domain.announcement.application;

import CaffeineCoder.recipic.domain.announcement.dao.AnnouncementRepository;
import CaffeineCoder.recipic.domain.announcement.domain.Announcement;
import CaffeineCoder.recipic.domain.announcement.dto.AnnouncementRequestDto;
import CaffeineCoder.recipic.domain.announcement.dto.AnnouncementResponseDto;
import CaffeineCoder.recipic.global.image.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final ImageService imageService;


    public List<AnnouncementResponseDto> getAnnouncements(int page, int size) {
        Page<Announcement> announcementPage = announcementRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return announcementPage.getContent().stream()
                .map(this::getAnnouncementToAnnouncementResponseDto)
                .collect(Collectors.toList());
    }

    public AnnouncementResponseDto getAnnouncement(Long announcementId) {
        return getAnnouncementToAnnouncementResponseDto(announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("Announcement not found")));
    }

    private AnnouncementResponseDto getAnnouncementToAnnouncementResponseDto(Announcement announcement) {
        return AnnouncementResponseDto.of(announcement);
    }

    public AnnouncementResponseDto registAnnouncement(AnnouncementRequestDto announcementRequestDto, MultipartFile thumbnailImage) {
        String uuid = null;
        try {
            if (!thumbnailImage.isEmpty()) {
                uuid = "https://storage.googleapis.com/recipick-image-bucket/" + imageService.uploadImage(thumbnailImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Announcement announcement = Announcement.builder()
                .title(announcementRequestDto.getTitle())
                .description(announcementRequestDto.getDescription())
                .thumbnailUrl(uuid)
                .build();

        return AnnouncementResponseDto.of(announcementRepository.save(announcement));
    }

}
