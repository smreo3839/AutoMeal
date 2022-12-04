package org.zerock.ex01.service;


import org.zerock.ex01.dto.*;
import org.zerock.ex01.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public interface MealPlanService {
    default MealPlan dtoToMealPlanEntity(MealPlanResulttDTO mealPlanResulttDTO, String userEmail) {
        User user = User.builder().userEmail(userEmail).build();

        MealPlan mealPlan = MealPlan.builder()
                .calories(mealPlanResulttDTO.getNutrients().getCalories())
                .carbohydrates(mealPlanResulttDTO.getNutrients().getCarbohydrates())
                .fat(mealPlanResulttDTO.getNutrients().getFat())
                .protein(mealPlanResulttDTO.getNutrients().getProtein())
                .user(user)
                .build();

        return mealPlan;
    }

    default MealPlanList dtoToMealPlanListEntity(MealPlanDTO mealPlanDTO, Long mealPlanid) {
        MealPlan mealPlan = MealPlan.builder().planId(mealPlanid).build();
        MealPlanList mealPlanList = MealPlanList.builder()
                .recipeId(mealPlanDTO.getId())
                .imageType(mealPlanDTO.getImageType())
                .title(mealPlanDTO.getTitle())
                .readyInMinutes(mealPlanDTO.getReadyInMinutes())
                .servings(mealPlanDTO.getServings())
                .mealPlan(mealPlan)
                .build();

        return mealPlanList;
    }

    default MealPlanResulttDTO entityToMealPlanResulttDto(MealPlan entity) {
        NutrientsDTO nutrientsDTO = NutrientsDTO
                .builder()
                .calories(entity.getCalories())
                .carbohydrates(entity.getCarbohydrates())
                .fat(entity.getFat())
                .protein(entity.getProtein())
                .build();
        MealPlanResulttDTO dto = MealPlanResulttDTO.builder()
                .nutrients(nutrientsDTO)
                .meals(entity.getMealPlanListList().stream().map(ent -> entityToMealPlanDTO(ent)).collect(Collectors.toList()))
                .plan_id(entity.getPlanId())
                .build();
        return dto;
    }

    default MealPlanDTO entityToMealPlanDTO(MealPlanList entity) {
        MealPlanDTO dto = new MealPlanDTO()
                .builder()
                .id(entity.getPlanListId())
                .imageType(entity.getImageType())
                .title(entity.getTitle())
                .readyInMinutes(entity.getReadyInMinutes())
                .servings(entity.getServings())
                .clear_state(entity.isClearState())
                .recipeId(entity.getRecipeId())
                .build();
        return dto;
    }

    List<Object> generateMealPlan(GenerateMealPlanDTO generateMealPlanDTO);

    void addMealPlan(String userEmail, List<MealPlanResulttDTO> mealPlanResultList);

    Long checkMealPlanCount(String userEmail);

    void removeMealPlan(Long planId);

    List<MealPlanResulttDTO> getMealPlan(String userEmail);

    void ChangeStatus(Long planListId);

    void StatusAllReset(String userEmail);
}
