package CaffeineCoder.recipic.domain.recipe.application;

import CaffeineCoder.recipic.domain.brand.repository.BrandRepository;
import CaffeineCoder.recipic.domain.brand.repository.IngredientRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentLikeRepository;
import CaffeineCoder.recipic.domain.comment.dao.CommentRepository;
import CaffeineCoder.recipic.domain.comment.domain.Comment;
import CaffeineCoder.recipic.domain.comment.dto.CommentDto;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeIngredientRepository;
import CaffeineCoder.recipic.domain.recipe.dao.RecipeRepository;
import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredient;
import CaffeineCoder.recipic.domain.recipe.domain.RecipeIngredientId;
import CaffeineCoder.recipic.domain.recipe.dto.*;
import CaffeineCoder.recipic.domain.scrap.dao.ScrapRepository;
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


    public void registerRecipe(RecipeRequestDto recipeRequestDto) {
        Recipe recipe = Recipe.builder()
                .userId(Long.valueOf(recipeRequestDto.getUserId()))
                .brandId(recipeRequestDto.getBrandId())
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
                    RecipeIngredientId recipeIngredientId = new RecipeIngredientId(
                            selectedRecipe.getIngredientId(),
                            savedRecipe.getRecipeId()
                    );

                    return RecipeIngredient.builder()
                            .id(recipeIngredientId)
                            .recipe(savedRecipe)
                            .ingredient(ingredientRepository.findById(selectedRecipe.getIngredientId())
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + selectedRecipe.getIngredientId())))
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

        List<IncludeIngredientDto> IncludeIngredients = ingredients.stream()
                .map(ingredient -> IncludeIngredientDto.builder()
                        .ingredientId(ingredient.getIngredient().getIngredientId())
                        .count(ingredient.getCount())
                        .build())
                .collect(Collectors.toList());

        List<Comment> comments = commentRepository.findByRecipeId(recipeId);

        // Map Comment entities to CommentDto with like count
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> {
                    int likeCount = commentLikeRepository.countByCommentId(comment.getCommentId());
                    return CommentDto.fromEntity(comment, likeCount);  // 좋아요 수와 함께 매핑
                })
                .collect(Collectors.toList());

        return RecipeDetailResponseDto.builder()
                .recipeId(recipe.getRecipeId().toString())
                .userId(recipe.getUserId().toString())
                .brandId(recipe.getBrandId().toString())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .imageUrl(recipe.getImageUrl())
                .isCelebrity(recipe.getIsCelebrity().toString())
                .createdAt(recipe.getCreatedAt().toString())
                .status(recipe.getStatus().toString())
                .scrapCount(scrapCount)
                .IncludeIngredients(IncludeIngredients)
                .comments(commentDtos)
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

        // 수정할 게시글을 찾기
        Recipe recipe = recipeRepository.findById(Integer.parseInt(recipeRequestDto.getRecipeId()))
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        // 게시글 작성자와 현재 사용자가 같은지 확인
        if (!recipe.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this recipe");
        }

        // 게시글 수정
        recipe.updateRecipe(
                recipeRequestDto.getTitle(),
                recipeRequestDto.getDescription(),
                recipeRequestDto.getThumbnailUrl(),
                recipeRequestDto.getBrandId(),
                recipeRequestDto.getIsCelebrity()
        );

        recipeRepository.save(recipe);
        return true;
    }
}
