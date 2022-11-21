package org.zerock.ex01.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex01.dto.RecipeBookMarkDTO;
import org.zerock.ex01.service.MainRecipeService;


@SpringBootTest
public class RecipeBookMarkRepositoryTests {
    @Autowired
    RecipeBookMarkRepository recipeBookMarkRepository;
    @Autowired
    MainRecipeService mainRecipeService;



}
