package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.brand.api.BrandService;
import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import CaffeineCoder.recipic.domain.brand.repository.IngredientRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentLikeRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.comment.dto.CommentDto;
import CaffeineCoder.recipic.domain.jwtSecurity.util.SecurityUtil;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeIngredientRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.brand.domain.Ingredient;

import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredientId;
import CaffeineCoder.recipic.domain.recipe.dto.*;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
import CaffeineCoder.recipic.domain.user.dao.UserRepository;
import CaffeineCoder.recipic.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
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
    private final UserRepository userRepository;
    private final BrandService brandService;


    public void registerRecipe(RecipeRequestDto recipeRequestDto) {
        Long userId = SecurityUtil.getCurrentMemberId();

        // brandName으로 brandId 찾기
        Integer brandId = brandRepository.findBrandIdByBrandName(recipeRequestDto.getBrandName())
                .orElseThrow(() -> new RuntimeException("Brand not found: " + recipeRequestDto.getBrandName()));

        Recipe recipe = Recipe.builder()
                .userId(userId)
                .brandId(brandId)  // brandName 대신 brandId 설정
                .title(recipeRequestDto.getTitle())
                .description(recipeRequestDto.getDescription())
                .imageUrl(recipeRequestDto.getThumbnailUrl())
                .isCelebrity(recipeRequestDto.getIsCelebrity())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(1)
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeIngredient> recipeIngredients = recipeRequestDto.getSelectedRecipes().stream()
                .map(selectedRecipe -> {
                    // ingredientName으로 ingredientId 찾기
                    Integer ingredientId = ingredientRepository.findByName(selectedRecipe.getIngredientName())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + selectedRecipe.getIngredientName()));

                    RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                            ingredientId,
                            savedRecipe.getRecipeId()
                    );

                    return RecipeIngredient.builder()
                            .id(recipeIngredientId)
                            .recipe(savedRecipe)
                            .ingredient(ingredientRepository.findById(ingredientId)
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientId)))
                            .count(selectedRecipe.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);
    }


    public RecipeDetailResponseDto getRecipeDetail(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        // Fetch scrap count
        int scrapCount = scrapRepository.countByRecipeId(recipeId);

        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipeId);

        List<IncludeIngredientDto> includeIngredients = ingredients.stream()
                .map(ingredient -> {
                    // Get the ingredientId
                    Integer ingredientId = ingredient.getIngredient().getIngredientId();

                    // Find Ingredient using ingredientId from the repository
                    Ingredient foundIngredient = ingredientRepository.findById(ingredientId)
                            .orElse(null);  // 해당 재료가 없을 경우 null 처리

                    // Build IncludeIngredientDto with Ingredient object and count
                    return IncludeIngredientDto.builder()
                            .ingredient(foundIngredient)  // Ingredient 객체 자체를 포함
                            .count(ingredient.getCount())  // 해당 재료의 수량 포함
                            .build();
                })
                .collect(Collectors.toList());



        Optional<User> OptionalUser = userRepository.findById(recipe.getUserId());
        User user = OptionalUser.orElseThrow(() -> new RuntimeException("User not found"));

        String brandName = brandService.getBrandNameByBrandId(recipe.getBrandId());

        return RecipeDetailResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .userNickName(user.getNickName())
                .userProfileImageUrl(user.getProfileImageUrl()) // Set the profile image URL here
                .brandName(brandName)
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .thunbnailUrl(recipe.getImageUrl())
                .isCelebrity(recipe.getIsCelebrity().toString())
                .createdAt(recipe.getCreatedAt().toString())
                .status(recipe.getStatus().toString())
                .scrapCount(scrapCount)
                .IncludeIngredients(includeIngredients)
                .build();
    }




    public List<?> getQueriedRecipes(String keyword, int page, int size) {
        if(keyword.equals("-1")){
            return getAllRecipes(page, size);
        }

        Optional<Integer> brandId= brandRepository.findBrandIdByBrandName(keyword);

        if(brandId.isEmpty()){
            return Collections.emptyList();
        }

        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByBrandId(brandId.get(), PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt")));

        List<RecipeDto> recipeDtos = recipeDtoPage.getContent();

        List<RecipeResponseDto> recipeResponseDtos = recipeDtos.stream()
                .map(recipeDto -> {
                    int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
                    int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
                    return RecipeResponseDto.fromDto(recipeDto, scrapCount, commentCount);
                })
                .collect(Collectors.toList());

        return recipeResponseDtos;

    }

    public List<RecipeResponseDto> getAllRecipes(int page, int size) {

        Page<Recipe> recipePage = recipeRepository.findAll(PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt")));

        List<Recipe> recipes = recipePage.getContent();

        List<RecipeResponseDto> recipeResponseDtos = recipes.stream()
                .map(recipe -> {
                    int scrapCount = scrapRepository.countByRecipeId(recipe.getRecipeId());
                    int commentCount = commentRepository.countByRecipeId(recipe.getRecipeId());
                    return RecipeResponseDto.fromEntity(recipe, scrapCount,commentCount);
                })
                .collect(Collectors.toList());

        return recipeResponseDtos;
    }

    public List<RecipeResponseDto> getUserQueriedRecipes(String keyword, int page, int size, Long userId) {
        if(keyword.equals("-1")){
            return getAllUserRecipes(page, size, userId);
        }

        Optional<Integer> brandId= brandRepository.findBrandIdByBrandName(keyword);

        if(brandId.isEmpty()){
            return Collections.emptyList();
        }

        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByBrandIdAndUserId(brandId.get(), userId, PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt")));

        List<RecipeDto> recipeDtos = recipeDtoPage.getContent();

        List<RecipeResponseDto> recipeResponseDtos = recipeDtos.stream()
                .map(recipeDto -> {
                    int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
                    int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
                    return RecipeResponseDto.fromDto(recipeDto, scrapCount, commentCount);
                })
                .collect(Collectors.toList());

        return recipeResponseDtos;

    }

    public List<RecipeResponseDto> getAllUserRecipes(int page, int size, Long userId) {
        Page<RecipeDto> recipeDtoPage = recipeRepository.findRecipesByUserId(userId, PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt")));

        List<RecipeDto> recipeDtos = recipeDtoPage.getContent();

        List<RecipeResponseDto> recipeResponseDtos = recipeDtos.stream()
                .map(recipeDto -> {
                    int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
                    int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
                    return RecipeResponseDto.fromDto(recipeDto, scrapCount, commentCount);
                })
                .collect(Collectors.toList());

        return recipeResponseDtos;
    }

    public List<RecipeResponseDto> getUserQueriedScrapedRecipes(String keyword, int page, int size, List<Integer> recipeIds) {
        if(keyword.equals("-1")){
            return getUserAllScrapedRecipes(page, size, recipeIds);
        }

        Optional<Integer> brandId= brandRepository.findBrandIdByBrandName(keyword);

        if(brandId.isEmpty()){
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDto> recipeDtoPages = recipeRepository.findRecipesByBrandIdAndRecipeIds(
                brandId.get(), recipeIds, pageable);

        List<RecipeDto> recipeDtos = recipeDtoPages.getContent();

        List<RecipeResponseDto> recipeResponseDtos = recipeDtos.stream()
                .map(recipeDto -> {
            int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
            int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
                    return RecipeResponseDto.fromDto(recipeDto, scrapCount, commentCount);
                })
                .collect(Collectors.toList());

        return recipeResponseDtos;
    }

    public List<RecipeResponseDto> getUserAllScrapedRecipes(int page, int size, List<Integer> recipeIds) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDto> recipeDtoPages = recipeRepository.findRecipesByRecipeIds(recipeIds, pageable);

        List<RecipeDto> recipeDtos = recipeDtoPages.getContent();

        List<RecipeResponseDto> recipeResponseDtos = recipeDtos.stream()
                .map(recipeDto -> {
                    int scrapCount = scrapRepository.countByRecipeId(recipeDto.recipeId());
                    int commentCount = commentRepository.countByRecipeId(recipeDto.recipeId());
                    return RecipeResponseDto.fromDto(recipeDto, scrapCount, commentCount);
                })
                .collect(Collectors.toList());

        return recipeResponseDtos;
    }





    @Transactional
    public boolean deleteRecipe(Integer recipeId) {
        // 현재 인증된 사용자의 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 해당 ID의 레시피 찾기
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
    public boolean updateRecipe(RecipeRequestDto recipeRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        // 수정할 레시피를 찾기
        Recipe recipe = recipeRepository.findById(Integer.valueOf(recipeRequestDto.getRecipeId()))
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        // 레시피 작성자와 현재 사용자가 같은지 확인
        if (!recipe.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this recipe");
        }

        // brandName으로 brandId 찾기
        Integer brandId = brandRepository.findBrandIdByBrandName(recipeRequestDto.getBrandName())
                .orElseThrow(() -> new RuntimeException("Brand not found: " + recipeRequestDto.getBrandName()));

        // 레시피 수정
        recipe.updateRecipe(
                recipeRequestDto.getTitle(),
                recipeRequestDto.getDescription(),
                recipeRequestDto.getThumbnailUrl(),
                brandId,  // brandId 설정
                recipeRequestDto.getIsCelebrity()
        );

        // 레시피 저장
        recipeRepository.save(recipe);

        // 기존 재료 삭제 후 재추가
        recipeIngredientRepository.deleteByRecipeId(recipe.getRecipeId());

        List<RecipeIngredient> recipeIngredients = recipeRequestDto.getSelectedRecipes().stream()
                .map(selectedRecipe -> {
                    // ingredientName으로 ingredientId 찾기
                    Integer ingredientId = ingredientRepository.findByName(selectedRecipe.getIngredientName())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + selectedRecipe.getIngredientName()));

                    RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                            ingredientId,
                            recipe.getRecipeId()
                    );

                    return RecipeIngredient.builder()
                            .id(recipeIngredientId)
                            .recipe(recipe)
                            .ingredient(ingredientRepository.findById(ingredientId)
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientId)))
                            .count(selectedRecipe.getCount())
                            .build();
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);

        return true;
    }

}
