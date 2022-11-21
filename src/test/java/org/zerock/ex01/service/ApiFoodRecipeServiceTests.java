package org.zerock.ex01.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.entity.User;

@SpringBootTest
public class ApiFoodRecipeServiceTests {
    @Autowired
    ApiFoodRecipeService service;

    @Test
    public void checkBookMark() {
        UserDTO user = UserDTO.builder().userEmail("test10@gmail.com").build();
        service.searchRecipes(user, "pasta");
    }
}
