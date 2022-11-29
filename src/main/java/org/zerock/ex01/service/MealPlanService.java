package org.zerock.ex01.service;

import org.zerock.ex01.dto.MealPalnDTO;

import java.util.Map;

public interface MealPlanService {
    Map<String, Object> generateMealPlan(MealPalnDTO mealPalnDTO);

    void addMealPlan(Map<String, Object> mealPlanResult);
}
