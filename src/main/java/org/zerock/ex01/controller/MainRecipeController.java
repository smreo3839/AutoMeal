package org.zerock.ex01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.ex01.dto.MainRecipeDTO;
import org.zerock.ex01.dto.RecipeBookMarkDTO;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.service.ApiFoodRecipeServiceImpl;
import org.zerock.ex01.service.MainRecipeService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/RecipeDB")
@Log4j2
@RequiredArgsConstructor//의존성 자동주입, final 혹은 @NotNull 필드만 의존성 자동 주입이 가능하다
public class MainRecipeController {
    private final ApiFoodRecipeServiceImpl apiFoodRecipeService;
    private final MainRecipeService mainRecipeService;

    @GetMapping("random_recipe")
    public Map random_recipe() {//랜덤레시피 정보
        log.info("random_recipe");
        return apiFoodRecipeService.getRandomRecipe();
    }

    @GetMapping("detail_recipe")
    public Map getDetailRecipe(String recipeId) {//해당 recipeId에 해당하는 상세 정보(레시피 id)
        log.info("detail_recipe");
        return apiFoodRecipeService.getDetailRecipe(recipeId);
    }

    @PostMapping("searchRecipes")
    public Map searchRecipes(UserDTO user, MainRecipeDTO dto, @AuthenticationPrincipal String userEmail) {//해당 음식 이름에 해당하는 레시피들 정보
        log.info("searchRecipes");
        user.setUserEmail(userEmail);
        log.info("dto check{}", dto.getIncludeIngredients());
        return apiFoodRecipeService.searchRecipes(user, dto);
    }

    @PostMapping("searchRecipesNotUser")
    public Map searchRecipes(UserDTO user, MainRecipeDTO dto) {//해당 음식 이름에 해당하는 레시피들 정보 비로그인 유저 전용
        log.info("searchRecipes");
        log.info("dto check{}", dto.getIncludeIngredients());
        return apiFoodRecipeService.searchRecipes(user, dto);
    }

    @PostMapping("ChangeBookmark")
    public ResponseEntity<Boolean> changeBookmark(RecipeBookMarkDTO dto) {//해당 recipeId 레시피를 북마크 등록 및 취소
        log.info("changeBookmark");
        mainRecipeService.ChangeBookmark(dto);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("ChangeRecipeDone")
    public ResponseEntity<Boolean> changeRecipeDone(RecipeBookMarkDTO dto) {//해당 recipeId 레시피를 made It 등록 및 취소
        log.info("changeRecipeDone");
        mainRecipeService.ChangeRecipeDone(dto);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("ingredientDetection")
    public List<?> ingredientDetection(MultipartFile[] uploadFile) {//이미지 식별기능으로 식재료 식별
//        log.info((uploadFiles[0].getBytes()).toString());
//        log.info(Arrays.toString(uploadFiles[0].getBytes()));
//        log.info(new String(uploadFiles[0].getBytes(), StandardCharsets.UTF_8));
//        return null;
        return apiFoodRecipeService.ingredientDetection(uploadFile);
    }

    @PostMapping("foodImageClassificationNotUser")
    public ResponseEntity<?> foodImageClassification(UserDTO user, MultipartFile uploadFile) {//이미지로 음식 판별 비로그인 전용
        return ResponseEntity.ok().body(apiFoodRecipeService.foodImageClassification(user, uploadFile));
    }

    @PostMapping("foodImageClassification")
    public ResponseEntity<?> foodImageClassification(UserDTO user, MultipartFile uploadFile, @AuthenticationPrincipal String userEmail) {//이미지로 음식  판별
        log.info("searchRecipes");
        user.setUserEmail(userEmail);
        apiFoodRecipeService.foodImageClassification(user, uploadFile);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


}