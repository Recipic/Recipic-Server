package CaffeineCoder.recipic.domain.scrap.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> toggleScrap(@RequestBody Map<String, Integer> request) {
        Integer recipeId = request.get("recipeId");
        boolean isScrapped = scrapService.toggleScrap(recipeId);

        Map<String, Object> response = new HashMap<>();
        response.put("isScrapped", isScrapped);
        return ResponseEntity.ok(response);
    }
}

