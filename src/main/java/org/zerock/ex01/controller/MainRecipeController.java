package org.zerock.ex01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.jni.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex01.dto.RecipeBookMarkDTO;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.service.ApiFoodRecipeServiceImpl;
import org.zerock.ex01.service.MainRecipeService;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public Map searchRecipes(UserDTO user, @RequestParam("query") String query) {//해당 recipeId에 해당하는 상세 정보(레시피 id)
        log.info("searchRecipes");
        return apiFoodRecipeService.searchRecipes(user, query);
    }

    @PostMapping("ChangeBookmark")
    public ResponseEntity<Boolean> changeBookmark(RecipeBookMarkDTO dto) {//해당 recipeId에 해당하는 상세 정보(레시피 id,유저 이메일)
        log.info("changeBookmark");
        mainRecipeService.ChangeBookmark(dto);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("ChangeRecipeDone")
    public ResponseEntity<Boolean> changeRecipeDone(RecipeBookMarkDTO dto) {//해당 recipeId에 해당하는 상세 정보(레시피 id,유저 이메일)
        log.info("changeRecipeDone");
        mainRecipeService.ChangeRecipeDone(dto);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
