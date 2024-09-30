package CaffeineCoder.recipic.domain.announcement.api;

import CaffeineCoder.recipic.domain.announcement.application.AnnouncementService;
import CaffeineCoder.recipic.domain.announcement.dto.AnnouncementRequestDto;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeRequestDto;
import CaffeineCoder.recipic.global.response.ApiResponse;
import CaffeineCoder.recipic.global.util.ApiUtils;
import com.google.protobuf.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementAPI {
    private final AnnouncementService announcementService;

    @GetMapping("/list")
    public ApiResponse<?> announcementList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ApiUtils.success(announcementService.getAnnouncements(page, size));
    }

    @GetMapping("/detail")
    public ApiResponse<?> announcementDetail(@RequestParam("announcementId") Long announcementId) {
        return ApiUtils.success(announcementService.getAnnouncement(announcementId));
    }

    @PostMapping("/register")
    public ApiResponse<?> registerAnnouncement(
            @RequestPart(value="announcement")AnnouncementRequestDto announcementRequestDto,
            @RequestPart(value="thumbnailImage", required = false) MultipartFile thumbnailImage
    ) {
        return ApiUtils.success(announcementService.registAnnouncement(announcementRequestDto, thumbnailImage));
    }
}
