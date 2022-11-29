package org.zerock.ex01.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.MealPalnDTO;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.repository.MealPlanListRepostiory;
import org.zerock.ex01.repository.MealPlanRepostiory;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class MealPlanServiceImpl implements MealPlanService {
    private final ApiSendManager<MealPalnDTO> apiSendManager;
    private final MealPlanRepostiory mealPlanRepostiory;
    private final MealPlanListRepostiory mealPlanListRepostiory;
    @Value("${apikeystoDB}")
    private String apiKeysToDB;

    @Override
    public Map<String, Object> generateMealPlan(MealPalnDTO mealPalnDTO) {//하루 또는 주 레시피 생생 후 저장
        return apiSendManager.sendGetRequestToApi(apiKeysToDB, "https://api.spoonacular.com/mealplanner/generate?" + apiSendManager.dtoConvertStringParam(mealPalnDTO));
    }

    @Override
    public void addMealPlan(Map<String, Object> mealPlanResult) {
        UserDTO userDTO = (UserDTO) mealPlanResult.get("user");
        Map<String, Map<String, Object>> planRs = (Map<String, Map<String, Object>>) mealPlanResult.get("week");
        List<String> list = new ArrayList<>();

        for (String key : planRs.keySet()) {//monday,tuesday...
            List<Map<String, String>> meals = (List) planRs.get(key).get("meals");
            //List<?> tempList = tempList = meals.stream().map(s -> s.entrySet().stream().filter(e -> "id".equals(e))).collect(Collectors.toList());
            //   List<?> tempList = tempList = meals.stream().map(s -> String.valueOf(s.get("id"))).collect(Collectors.toList());
            meals.stream().flatMap(m -> m.entrySet().stream()).filter(f -> f.getKey().equals("id")).forEach(x -> list.add(String.valueOf(x.getValue())));
        }
        log.info("finalRs {}", list);
        //        planRs.entrySet().stream().forEacwh(entry -> entry.e);
    }

}