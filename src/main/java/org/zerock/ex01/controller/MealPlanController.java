package org.zerock.ex01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex01.dto.MealPalnDTO;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.service.MealPlanService;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/MealPlan")
@Log4j2
@RequiredArgsConstructor//의존성 자동주입, final 혹은 @NotNull 필드만 의존성 자동 주입이 가능하다
public class MealPlanController {
    private final MealPlanService mealPlanService;

    @GetMapping("generateMealPlan")
    public ResponseEntity<?> generateMealPlan(MealPalnDTO mealPalnDTO) {
        return ResponseEntity.ok().body(mealPlanService.generateMealPlan(mealPalnDTO));
    }

    @PostMapping("addMealPlan")
    public ResponseEntity<?> addMealPlan(@RequestBody Map<String, Object> mealPlanResult) {
        log.info(mealPlanResult);
        mealPlanService.addMealPlan(mealPlanResult);
        return ResponseEntity.ok().body(null);
    }
}
