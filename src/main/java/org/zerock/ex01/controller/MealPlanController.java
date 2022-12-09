package org.zerock.ex01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.GenerateMealPlanDTO;
import org.zerock.ex01.dto.MealPlanResulttDTO;
import org.zerock.ex01.service.MealPlanService;

import java.util.List;


@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/MealPlan")
@Log4j2
@RequiredArgsConstructor//의존성 자동주입, final 혹은 @NotNull 필드만 의존성 자동 주입이 가능하다
public class MealPlanController {
    private final MealPlanService mealPlanService;

    @GetMapping("/checkMealPlanCount")
    public ResponseEntity<?> checkMealPlanCount(@AuthenticationPrincipal String userId) {//Meal plan 개수 체크하기
        log.info("checkMealPlanCount_userId {}", userId);
        Long rsCount = mealPlanService.checkMealPlanCount(userId);
        return ResponseEntity.ok().body(rsCount < 7 ? rsCount == 0 ? "true" : "onlyDay" : "false");//true면 추가 가능
    }

    @PostMapping("/nser/generateMealPlan")
    public List<Object> generateMealPlan(GenerateMealPlanDTO generateMealPlanDTO) {
        log.info("generateMealPlanDTO {}", generateMealPlanDTO);
        return mealPlanService.generateMealPlan(generateMealPlanDTO);
    }

    @PostMapping("/addMealPlan")
    public ResponseEntity<?> addMealPlan(@AuthenticationPrincipal String userId, @RequestBody List<MealPlanResulttDTO> mealPlanRequest) {
        log.info("addMealPlan {}", mealPlanRequest);
        mealPlanService.addMealPlan(userId, mealPlanRequest);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/getMealPlan")
    public ResponseEntity<?> getMealPlan(@AuthenticationPrincipal String userId) {//유저의 MealPlan select
        log.info("getMealPlan {}", userId);
        return ResponseEntity.ok().body(mealPlanService.getMealPlanList(userId));
    }

    @GetMapping("/removeMealPlan")
    public ResponseEntity<?> removeMealPlan(@AuthenticationPrincipal String userId, Long planId) {
        log.info("removeMealPlan {}", userId);
        MealPlanResulttDTO mealPlanResulttDTO = mealPlanService.getMealPlanWithPlanId(planId);
        if (!mealPlanResulttDTO.getUserEmail().equals(userId))
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        mealPlanService.removeMealPlan(planId);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/nser/ChangeStatus")
    public ResponseEntity<?> ChangeStatus(Long planListId) {
        mealPlanService.ChangeStatus(planListId);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("StatusAllReset")
    public ResponseEntity<?> StatusAllReset(@AuthenticationPrincipal String userId) {
        mealPlanService.StatusAllReset(userId);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
