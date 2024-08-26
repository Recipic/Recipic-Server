package CaffeineCoder.recipic.domain.scrap.api;

import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import CaffeineCoder.recipic.domain.scrap.domain.Scrap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    @Transactional
    public boolean toggleScrap(Integer recipeId) {
        // 현재 인증된 사용자의 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 사용자가 이미 이 레시피를 스크랩했는지 확인
        Optional<Scrap> existingScrap = scrapRepository.findByUserIdAndRecipeId(userId, recipeId);

        if (existingScrap.isPresent()) {
            // 이미 스크랩했다면 스크랩을 취소
            scrapRepository.deleteByUserIdAndRecipeId(userId, recipeId);
            return false; // 스크랩 취소 false 반환
        } else {
            // 스크랩하지 않았다면 새 스크랩을 추가
            Scrap scrap = Scrap.builder()
                    .userId(userId)
                    .recipeId(recipeId)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            scrapRepository.save(scrap);
            return true; // 스크랩 추가 true 반환
        }
    }
}
