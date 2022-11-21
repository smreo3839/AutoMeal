package org.zerock.ex01.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.entity.RecipeBookMark;
import org.zerock.ex01.repository.RecipeBookMarkRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class ApiFoodRecipeServiceImpl implements ApiFoodRecipeService {
    private final RecipeBookMarkRepository recipeBookMarkRepository;

    @Override
    public Map<String, Object> getRandomRecipe() {
        log.info("getRandomRecipe");
        Map<String, Object> map = sendGetRequestToApi("https://api.spoonacular.com/recipes/random?number=20");
        List<Object> list = (List<Object>) map.get("recipes");
        return map;
    }

    @Override
    public Map<String, Object> getDetailRecipe(String recipeId) {
        log.info("getDetailRecipe");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Recipe_Information", sendGetRequestToApi("https://api.spoonacular.com/recipes/" + recipeId + "/information?includeNutrition=true"));
        result.put("Recipe_Taste", sendGetRequestToApi("https://api.spoonacular.com/recipes/" + recipeId + "/tasteWidget.json"));
        return result;
    }

    @Override
    public Map<String, Object> searchRecipes(UserDTO dto, String query) {
        log.info("searchRecipes");
        Map<String, Object> map = sendGetRequestToApi("https://api.spoonacular.com/recipes/complexSearch?query=" + query + "&number=100&sort=popularity");
        log.info(map);
        //map = checkBookMark(dto, map);
        if (dto != null) {
            map.put("BookMarkList", recipeBookMarkRepository.findAllBookMark(dto.getUser_email()));
        }
        return map;
    }

    public Map<String, Object> checkBookMark(UserDTO dto, Map<String, Object> map) {
        List<String> checkList = recipeBookMarkRepository.findAllBookMark(dto.getUser_email());
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("results");
        List Resultlist = list.stream().map(obj -> check(obj, checkList.contains(obj.get("id").toString()))).collect(Collectors.toList());
        log.info(Resultlist);
        return null;
    }

    public Map<String, Object> check(Map<String, Object> map, boolean flag) {
        map.put("checkBookMark", flag);
        return map;
    }

}
