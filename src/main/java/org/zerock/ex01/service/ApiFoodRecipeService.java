package org.zerock.ex01.service;


import org.springframework.web.multipart.MultipartFile;
import org.zerock.ex01.dto.MainRecipeDTO;
import org.zerock.ex01.dto.UserDTO;


import java.util.List;
import java.util.Map;

public interface ApiFoodRecipeService {

    Map<String, Object> getRandomRecipe();

    Map<String, Object> getDetailRecipe(String recipeId);


    Map<String, Object> searchRecipes(UserDTO userDTO, MainRecipeDTO recipeDTO);



    List<?> ingredientDetection(MultipartFile[] uploadFiles);
}
