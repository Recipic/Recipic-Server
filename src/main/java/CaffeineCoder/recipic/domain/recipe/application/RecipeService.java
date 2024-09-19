package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.brand.api.BrandService;
import CaffeineCoder.recipic.domain.brand.domain.BaseIngredient;
import CaffeineCoder.recipic.domain.brand.domain.Brand;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;
import CaffeineCoder.recipic.domain.brand.repository.BaseIngredientRepository;
import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import CaffeineCoder.recipic.domain.brand.repository.IngredientRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentLikeRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final BrandRepository brandRepository;
    private final IngredientRepository ingredientRepository;
    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BaseIngredientRepository baseIngredientRepository;
    private final UserRepository userRepository;
    private final BrandService brandService;
    private final ScrapService scrapService;
    private final UserService userService;
    private final ImageService imageService;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public void registerRecipe(RecipeRequestDto recipeRequestDto, MultipartFile thumbnailImage) {


    @Transactional
    public void registerRecipe(RecipeRequestDto recipeRequestDto) {
        Long userId = SecurityUtil.getCurrentMemberId();

        // brandId로 Brand 찾기
        Brand brand = brandRepository.findById(recipeRequestDto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        String uuid = null; // 유저가 이미지를 없애도록 수정한 경우

        try {
            if (!thumbnailImage.isEmpty()) {
                uuid = imageService.uploadImage(thumbnailImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        Recipe recipe = Recipe.builder()
                .userId(userId)
                .brand(brand)  // brandName 대신 brand 엔티티 직접 사용
                .title(recipeRequestDto.getTitle())
                .description(recipeRequestDto.getDescription())
                .imageUrl(uuid)
                .isCelebrity(recipeRequestDto.getIsCelebrity())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(1)
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        // BaseIngredient와 Ingredient 구분하여 처리
        List<RecipeIngredient> recipeIngredients = recipeRequestDto.getSelectedRecipes().stream()
                .map(selectedRecipe -> {
                    RecipeIngredient.RecipeIngredientBuilder recipeIngredientBuilder = RecipeIngredient.builder();
                    RecipeIngredientId recipeIngredientId;

                    if (selectedRecipe.isBaseIngredient()) {
                        BaseIngredient baseIngredient = baseIngredientRepository.findByIngredientName(selectedRecipe.getIngredientName())
                                .orElseThrow(() -> new RuntimeException("Base Ingredient not found: " + selectedRecipe.getIngredientName()));

                        recipeIngredientId = new RecipeIngredientId(baseIngredient.getIngredientName(), savedRecipe.getRecipeId());
                        recipeIngredientBuilder.baseIngredient(baseIngredient);
                    } else {
                        Ingredient ingredient = ingredientRepository.findByIngredientName(selectedRecipe.getIngredientName())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + selectedRecipe.getIngredientName()));

                        recipeIngredientId = new RecipeIngredientId(ingredient.getIngredientName(), savedRecipe.getRecipeId());
                        recipeIngredientBuilder.ingredient(ingredient);
                    }

                    return recipeIngredientBuilder
                            .id(recipeIngredientId)
                            .recipe(savedRecipe)
                            .count(selectedRecipe.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);
    }


    public RecipeDetailResponseDto getRecipeDetail(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        boolean isScrapped = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            Long currentMemberId = SecurityUtil.getCurrentMemberId();
            if (currentMemberId != null) {
                isScrapped = scrapService.isScrapped(currentMemberId, recipeId);
            }
        }

        int scrapCount = scrapRepository.countByRecipeId(recipeId);

        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipeId);
        List<IncludeIngredientDto> includeIngredients = ingredients.stream()
                .map(ingredient -> {
                    Integer ingredientId = ingredient.getIngredient().getIngredientId();
                    Ingredient foundIngredient = ingredientRepository.findById(ingredientId)
                            .orElse(null);

                    return IncludeIngredientDto.builder()
                            .ingredient(foundIngredient)
                            .count(ingredient.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        Optional<User> optionalUser = userRepository.findById(recipe.getUserId());
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

        String brandName = recipe.getBrandName();

        return RecipeDetailResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .userNickName(user.getNickName())
                .userProfileImageUrl(user.getProfileImageUrl())
                .brandName(brandName)
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .thunbnailUrl(recipe.getImageUrl())
                .isCelebrity(recipe.getIsCelebrity().toString())
                .createdAt(recipe.getCreatedAt().toString())
                .status(recipe.getStatus().toString())
                .isScrapped(isScrapped)
                .scrapCount(scrapCount)
                .IncludeIngredients(includeIngredients)
                .build();
    }

    @Transactional
    public boolean deleteRecipe(Integer recipeId) {
        // 현재 인증된 사용자의 ID를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 해당 ID의 레시피를 찾기
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        // 레시피 작성자와 현재 사용자가 같은지 확인
        if (!recipe.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this recipe");
        }

        // 레시피와 관련된 스크랩과 댓글 삭제
        scrapRepository.deleteByRecipeId(recipeId);
        commentRepository.deleteByRecipeId(recipeId);

        // 레시피 삭제
        recipeRepository.deleteById(recipeId);
        return true;
    }

    @Transactional
    public boolean updateRecipe(RecipeRequestDto recipeRequestDto, MultipartFile thumbnailImage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 수정할 레시피를 찾기
        Recipe recipe = recipeRepository.findById(Integer.valueOf(recipeRequestDto.getRecipeId()))
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        // 레시피 작성자와 현재 사용자가 같은지 확인
        if (!recipe.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this recipe");
        }

        // brandId로 Brand 찾기
        Brand brand = brandRepository.findById(recipeRequestDto.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

        // 레시피 수정

        String uuid = null; // 유저가 이미지를 없애도록 수정한 경우

        try {
            if (!thumbnailImage.isEmpty()) {
                uuid = imageService.uploadImage(thumbnailImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(uuid == null){
            uuid = recipe.getImageUrl();
        }

        recipe.updateRecipe(
                recipeRequestDto.getTitle(),
                recipeRequestDto.getDescription(),
                recipeRequestDto.getThumbnailUrl(),
                brand,
                uuid,
                brandId,  // brandId 설정
                recipeRequestDto.getIsCelebrity()
        );

        // 레시피 저장
        recipeRepository.save(recipe);

        // 기존 재료 삭제 후 재추가
        recipeIngredientRepository.deleteByRecipeId(recipe.getRecipeId());

        List<RecipeIngredient> recipeIngredients = recipeRequestDto.getSelectedRecipes().stream()
                .map(selectedRecipe -> {
                    String ingredientName = ingredientRepository.findByIngredientName(selectedRecipe.getIngredientName())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + selectedRecipe.getIngredientName()))
                            .getIngredientName();

                    RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                            ingredientName,
                            recipe.getRecipeId()
                    );

                    return RecipeIngredient.builder()
                            .id(recipeIngredientId)
                            .recipe(recipe)
                            .ingredient(ingredientRepository.findByIngredientName(ingredientName)
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientName)))
                            .count(selectedRecipe.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);

        return true;
    }


    public List<RecipeResponseDto> getQueriedRecipes(String keyword, int page, int size) {
        if (keyword.equals("-1")) {
            return getAllRecipes(page, size);
        }

        // brandName으로 검색
        String brandName = keyword;

        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByBrandName(brandName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<RecipeDto> recipeDtos = recipeDtoPage.getContent();

        return recipeDtos.stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    public List<RecipeResponseDto> getUserQueriedRecipes(String keyword, int page, int size, Long userId) {
        if (keyword.equals("-1")) {
            return getAllUserRecipes(page, size, userId);
        }

        // brandName으로 검색
        String brandName = keyword;

        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByBrandNameAndUserId(brandName, userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<RecipeDto> recipeDtos = recipeDtoPage.getContent();

        return recipeDtos.stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    public List<RecipeResponseDto> getUserQueriedScrapedRecipes(String keyword, int page, int size, List<Integer> recipeIds) {
        if (keyword.equals("-1")) {
            return getUserAllScrapedRecipes(page, size, recipeIds);
        }

        // brandName으로 검색
        String brandName = keyword;

        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByBrandNameAndRecipeIds(brandName, recipeIds, PageRequest.of(page, size));
        List<RecipeDto> recipeDtos = recipeDtoPage.getContent();

        return recipeDtos.stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }


    public List<RecipeResponseDto> getAllRecipes(int page, int size) {
        Page<Recipe> recipePage = recipeRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<Recipe> recipes = recipePage.getContent();

        return recipes.stream()
                .map(this::getRecipeToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    public List<RecipeResponseDto> getAllUserRecipes(int page, int size, Long userId) {
        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<RecipeDto> recipeDtos = recipeDtoPage.getContent();

        return recipeDtos.stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    public List<RecipeResponseDto> getUserAllScrapedRecipes(int page, int size, List<Integer> recipeIds) {
        Page<RecipeDto> recipeDtoPages = recipeRepository.findRecipesByRecipeIds(recipeIds, PageRequest.of(page, size));
        List<RecipeDto> recipeDtos = recipeDtoPages.getContent();

        return recipeDtos.stream()
                .map(this::getRecipeDtoToRecipeResponseDto)
                .collect(Collectors.toList());
    }

    private RecipeResponseDto getRecipeDtoToRecipeResponseDto(RecipeDto recipeDto) {
        int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
        int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
        User user = userService.findUser(recipeDto.userId());
        String brandName = recipeDto.brandName();

        return RecipeResponseDto.fromDto(recipeDto, user, brandName, scrapCount, commentCount);
    }

    private RecipeResponseDto getRecipeToRecipeResponseDto(Recipe recipe) {
        RecipeDto recipeDto = RecipeDto.fromEntity(recipe);
        int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
        int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
        User user = userService.findUser(recipeDto.userId());
        String brandName = recipeDto.brandName();

        return RecipeResponseDto.fromDto(recipeDto, user, brandName, scrapCount, commentCount);
    }
}
