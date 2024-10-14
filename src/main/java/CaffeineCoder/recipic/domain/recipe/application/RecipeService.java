package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.brand.api.BrandService;
import CaffeineCoder.recipic.domain.brand.domain.BaseIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Brand;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import CaffeineCoder.recipic.domain.brand.dto.BaseIngredientDTO;
import CaffeineCoder.recipic.domain.brand.dto.IngredientDTO;
import CaffeineCoder.recipic.domain.brand.repository.BaseIngredientRepository;
import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import CaffeineCoder.recipic.domain.brand.repository.IngredientRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.notification.application.NotificationService;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeIngredientRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredientId;
import CaffeineCoder.recipic.domain.recipe.dto.*;
import CaffeineCoder.recipic.domain.scrap.api.ScrapService;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import CaffeineCoder.recipic.domain.user.application.UserService;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import CaffeineCoder.recipic.global.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final BrandRepository brandRepository;
    private final IngredientRepository ingredientRepository;
    private final BaseIngredientRepository baseIngredientRepository;
    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BrandService brandService;
    private final ScrapService scrapService;
    private final ImageService imageService;
    private final NotificationService notificationService;
    private final RecipeUserHelper recipeUserHelper;


    @Transactional
    public void registerRecipe(RecipeRequestDto recipeRequestDto, MultipartFile thumbnailImage) {
        // 현재 사용자의 ID 가져오기
        Long userId = SecurityUtil.getCurrentMemberId();

        // 브랜드 정보 가져오기
        Brand brand = brandRepository.findByBrandName(recipeRequestDto.getBrandName())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        // BaseIngredient 필수 확인 및 가져오기
        BaseIngredient baseIngredient = baseIngredientRepository.findById(recipeRequestDto.getBaseIngredientId())
                .orElseThrow(() -> new RuntimeException("BaseIngredient not found"));

        // 이미지 업로드 처리
        String uuid = null;
        try {
            if (!thumbnailImage.isEmpty()) {
                uuid = "https://storage.googleapis.com/recipick-image-bucket/"+ imageService.uploadImage(thumbnailImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 레시피 생성
        Recipe recipe = Recipe.builder()
                .userId(userId)
                .brand(brand)
                .title(recipeRequestDto.getTitle())
                .description(recipeRequestDto.getDescription())
                .imageUrl(uuid)
                .isCelebrity(recipeRequestDto.getIsCelebrity())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(1)
                .build();

        // 레시피 저장
        Recipe savedRecipe = recipeRepository.save(recipe);

        // 선택된 재료들을 저장 (baseIngredient 포함)
        List<RecipeIngredient> recipeIngredients = recipeRequestDto.getSelectedIngredients().stream()
                .map(selectedIngredient -> {
                    // Ingredient 조회
                    Ingredient ingredient = ingredientRepository.findById(selectedIngredient.getIngredientId())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found"));

                    // ingredient_name 확인 (이 단계에서 ingredient_name이 제대로 설정되어 있어야 함)
                    if (ingredient.getIngredientName() == null || ingredient.getIngredientName().isEmpty()) {
                        throw new RuntimeException("Ingredient name is missing");
                    }

                    // RecipeIngredient 생성
                    RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                            savedRecipe.getRecipeId(),
                            ingredient.getIngredientId(),
                            baseIngredient.getBaseIngredientId()
                    );

                    return RecipeIngredient.builder()
                            .id(recipeIngredientId)
                            .recipe(savedRecipe)
                            .ingredient(ingredient)
                            .baseIngredient(baseIngredient)
                            .count(selectedIngredient.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);
    }

    public RecipeDetailResponseDto getRecipeDetail(Integer recipeId) {
        // 레시피 ID로 레시피를 찾아서 없으면 예외 처리
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        // 현재 사용자가 이 레시피를 스크랩한 적이 있는지 확인
        boolean isScrapped = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            Long currentMemberId = SecurityUtil.getCurrentMemberId();
            if (currentMemberId != null) { // 스크랩 여부 확인
                isScrapped = scrapService.isScrapped(currentMemberId, recipeId);
            }
        }

        // 스크랩 수 가져오기
        int scrapCount = scrapRepository.countByRecipeId(recipeId);

        // BaseIngredient 설정
        String baseIngredientName = recipeIngredientRepository.findByRecipeId(recipeId).stream()
                .findFirst()
                .map(ingredient -> ingredient.getBaseIngredient().getIngredientName())
                .orElse("Unknown Base Ingredient");

        // 레시피 재료 목록 가져오기 및 DTO 변환
        List<IncludeIngredientDto> includeIngredients = recipeIngredientRepository.findByRecipeId(recipeId).stream()
                .map(ingredient -> {
                    // IngredientDTO 생성
                    IngredientDTO ingredientDTO = IngredientDTO.builder()
                            .ingredientId(ingredient.getIngredient().getIngredientId())
                            .ingredientName(ingredient.getIngredient().getIngredientName())
                            .quantity(ingredient.getIngredient().getQuantity())
                            .unit(ingredient.getIngredient().getUnit())
                            .cost(ingredient.getIngredient().getCost())
                            .calorie(ingredient.getIngredient().getCalorie())
                            .build();

                    // IncludeIngredientDto 생성
                    return IncludeIngredientDto.builder()
                            .ingredient(ingredientDTO)
                            .count(ingredient.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        User user = recipeUserHelper.findUser(recipe.getUserId());  // RecipeUserHelper로 작성자 정보 가져오기

        // 레시피 상세 정보 DTO 반환
        return RecipeDetailResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .userNickName(user.getNickName())
                .userProfileImageUrl(user.getProfileImageUrl())
                .brandName(recipe.getBrandName())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .thunbnailUrl(recipe.getImageUrl())
                .isCelebrity(recipe.getIsCelebrity())
                .createdAt(recipe.getCreatedAt().toString())
                .status(recipe.getStatus().toString())
                .isScrapped(isScrapped)
                .scrapCount(scrapCount)
                .includeIngredients(includeIngredients)
                .baseIngredient(baseIngredientName)
                .build();
    }

    @Transactional
    public boolean deleteRecipe(Integer recipeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = SecurityUtil.getCurrentMemberId();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        if (!recipe.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this recipe");
        }

        scrapRepository.deleteByRecipeId(recipeId);
        commentRepository.deleteByRecipeId(recipeId);

        recipeRepository.deleteById(recipeId);
        return true;
    }

    @Transactional
    public boolean updateRecipe(RecipeRequestDto recipeRequestDto, MultipartFile thumbnailImage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = SecurityUtil.getCurrentMemberId();

        // 기존 레시피 찾기
        Recipe recipe = recipeRepository.findById(recipeRequestDto.getRecipeId())
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        // 현재 사용자와 레시피 작성자 일치 여부 확인
        if (!recipe.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this recipe");
        }

        // 브랜드는 brandName을 통해 조회
        Brand brand = brandRepository.findByBrandName(recipeRequestDto.getBrandName())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

        // BaseIngredient 필수 확인 및 가져오기
        BaseIngredient baseIngredient = baseIngredientRepository.findById(recipeRequestDto.getBaseIngredientId())
                .orElseThrow(() -> new RuntimeException("BaseIngredient not found"));

        // 이미지 업로드 처리
        String uuid = null;
        try {
            if (!thumbnailImage.isEmpty()) {
                uuid = "https://storage.googleapis.com/recipick-image-bucket/"+ imageService.uploadImage(thumbnailImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 이미지가 없는 경우 기존 이미지 사용
        if (uuid == null) {
            uuid = recipe.getImageUrl();
        }

        // 레시피 업데이트
        recipe.updateRecipe(
                recipeRequestDto.getTitle(),
                recipeRequestDto.getDescription(),
                uuid,
                brand,
                recipeRequestDto.getIsCelebrity()
        );

        recipeRepository.save(recipe);

        // 기존 재료 삭제 후, 새로운 재료 저장
        recipeIngredientRepository.deleteByRecipeId(recipe.getRecipeId());

        // 새로운 재료 추가
        List<RecipeIngredient> recipeIngredients = recipeRequestDto.getSelectedIngredients().stream()
                .map(selectedIngredient -> {
                    Ingredient ingredient = ingredientRepository.findById(selectedIngredient.getIngredientId())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found"));

                    RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                            recipe.getRecipeId(),
                            ingredient.getIngredientId(),
                            baseIngredient.getBaseIngredientId()
                    );

                    return RecipeIngredient.builder()
                            .id(recipeIngredientId)
                            .recipe(recipe)
                            .ingredient(ingredient)
                            .baseIngredient(baseIngredient)
                            .count(selectedIngredient.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);

        return true;
    }

    public List<RecipeResponseDto> getQueriedRecipes(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Recipe> recipePage;
        if (keyword.equals("-1")) {
            // 키워드가 없는 경우 전체 레시피 반환
            recipePage = recipeRepository.findAll(pageRequest);
        } else {
            // 키워드가 있는 경우 키워드를 기반으로 레시피 검색
            recipePage = recipeRepository.findByKeyword(keyword, pageRequest);
        }

        return recipePage.getContent().stream()
                .map(recipe -> RecipeResponseDto.fromEntity(
                        recipe,
                        recipeUserHelper.findUser(recipe.getUserId()),  // 레시피 작성자 정보 가져오기
                        recipe.getBrandName(),
                        recipeRepository.countScrapsByRecipeId(recipe.getRecipeId()),
                        commentRepository.countByRecipeId(recipe.getRecipeId())  // 댓글 수 추가
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> getUserQueriedRecipes(String keyword, int page, int size, Long userId) {
        if (keyword.equals("-1")) {
            return getAllUserRecipes(page, size, userId);
        }

        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByBrandNameAndUserId(keyword, userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return recipeDtoPage.getContent().stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> getUserQueriedScrapedRecipes(String keyword, int page, int size, List<Integer> recipeIds) {
        if (keyword.equals("-1")) {
            return getUserAllScrapedRecipes(page, size, recipeIds);
        }

        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByBrandNameAndRecipeIds(keyword, recipeIds, PageRequest.of(page, size));
        return recipeDtoPage.getContent().stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    private List<RecipeResponseDto> getAllRecipes(int page, int size) {
        Page<Recipe> recipePage = recipeRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return recipePage.getContent().stream()
                .map(this::getRecipeToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    private List<RecipeResponseDto> getAllUserRecipes(int page, int size, Long userId) {
        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return recipeDtoPage.getContent().stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    private List<RecipeResponseDto> getUserAllScrapedRecipes(int page, int size, List<Integer> recipeIds) {
        Page<RecipeDto> recipeDtoPages = recipeRepository.findRecipesByRecipeIds(recipeIds, PageRequest.of(page, size));
        List<RecipeDto> recipeDtos = recipeDtoPages.getContent();

        return recipeDtos.stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    private RecipeResponseDto getRecipeDtoToRecipeResponseDto(RecipeDto recipeDto) {
        int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
        int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
        User user = recipeUserHelper.findUser(recipeDto.userId());
        String brandName = recipeDto.brandName();

        return RecipeResponseDto.fromDto(recipeDto, user, brandName, scrapCount, commentCount);
    }

    private RecipeResponseDto getRecipeToRecipeResponseDto(Recipe recipe) {
        RecipeDto recipeDto = RecipeDto.fromEntity(recipe);
        int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
        int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
        User user = recipeUserHelper.findUser(recipeDto.userId());
        String brandName = recipeDto.brandName();

        return RecipeResponseDto.fromDto(recipeDto, user, brandName, scrapCount, commentCount);
    }

    // 유저 탈퇴 시 레시피 삭제 메서드
    public void deleteRecipesByUserId(Long userId) {
        List<Recipe> userRecipes = recipeRepository.findByUserId(userId);
        for (Recipe recipe : userRecipes) {
            recipeIngredientRepository.deleteByRecipeId(recipe.getRecipeId());
            commentRepository.deleteByRecipeId(recipe.getRecipeId());
            scrapRepository.deleteByRecipeId(recipe.getRecipeId());
        }
        recipeRepository.deleteByUserId(userId);
    }
}
