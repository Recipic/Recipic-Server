package CaffeineCoder.recipic.domain.recipe.dao;

import CaffeineCoder.recipic.domain.recipe.domain.Recipe;
import CaffeineCoder.recipic.domain.recipe.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query("SELECT new CaffeineCoder.recipic.domain.recipe.dto.RecipeDto(" +
            "r.recipeId, " +
            "r.userId, " +
            "b.brandName, " + // Brand 엔티티에서 brandName을 가져옴
            "r.title, " +
            "r.description, " +
            "r.imageUrl, " +
            "r.isCelebrity, " +
            "r.createdAt, " +
            "r.status) " +
            "FROM Recipe r JOIN r.brand b " + // Recipe와 Brand를 조인
            "WHERE r.userId = :userId")
    Page<RecipeDto> findRecipesByUserId(
            @Param("userId") Long userId,
            Pageable pageable);

    @Query("SELECT new CaffeineCoder.recipic.domain.recipe.dto.RecipeDto(" +
            "r.recipeId, " +
            "r.userId, " +
            "b.brandName, " + // Brand 엔티티에서 brandName을 가져옴
            "r.title, " +
            "r.description, " +
            "r.imageUrl, " +
            "r.isCelebrity, " +
            "r.createdAt, " +
            "r.status) " +
            "FROM Recipe r JOIN Brand b ON r.brand.brandId = b.brandId " + // Recipe와 Brand를 명시적으로 조인
            "WHERE b.brandName = :brandName")
    Page<RecipeDto> findRecipesByBrandName(@Param("brandName") String brandName, Pageable pageable);

    @Query("SELECT new CaffeineCoder.recipic.domain.recipe.dto.RecipeDto(" +
            "r.recipeId, " +
            "r.userId, " +
            "b.brandName, " + // Brand 엔티티에서 brandName을 가져옴
            "r.title, " +
            "r.description, " +
            "r.imageUrl, " +
            "r.isCelebrity, " +
            "r.createdAt, " +
            "r.status) " +
            "FROM Recipe r JOIN r.brand b " + // Recipe와 Brand를 조인
            "WHERE b.brandName = :brandName AND r.userId = :userId")
    Page<RecipeDto> findRecipesByBrandNameAndUserId(
            @Param("brandName") String brandName,
            @Param("userId") Long userId,
            Pageable pageable);

    @Query("SELECT new CaffeineCoder.recipic.domain.recipe.dto.RecipeDto(" +
            "r.recipeId, " +
            "r.userId, " +
            "b.brandName, " + // Brand 엔티티에서 brandName을 가져옴
            "r.title, " +
            "r.description, " +
            "r.imageUrl, " +
            "r.isCelebrity, " +
            "r.createdAt, " +
            "r.status) " +
            "FROM Recipe r JOIN r.brand b " + // Recipe와 Brand를 조인
            "WHERE b.brandName = :brandName AND r.recipeId IN :recipeIds")
    Page<RecipeDto> findRecipesByBrandNameAndRecipeIds(
            @Param("brandName") String brandName,
            @Param("recipeIds") List<Integer> recipeIds,
            Pageable pageable);

    @Query("SELECT new CaffeineCoder.recipic.domain.recipe.dto.RecipeDto(" +
            "r.recipeId, " +
            "r.userId, " +
            "b.brandName, " + // Brand 엔티티에서 brandName을 가져옴
            "r.title, " +
            "r.description, " +
            "r.imageUrl, " +
            "r.isCelebrity, " +
            "r.createdAt, " +
            "r.status) " +
            "FROM Recipe r JOIN r.brand b " + // Recipe와 Brand를 조인
            "WHERE r.recipeId IN :recipeIds")
    Page<RecipeDto> findRecipesByRecipeIds(
            @Param("recipeIds") List<Integer> recipeIds,
            Pageable pageable);

    // 유저 ID로 레시피 목록 조회
    List<Recipe> findByUserId(Long userId);

    // 유저 ID로 레시피 삭제
    void deleteByUserId(Long userId);
}
