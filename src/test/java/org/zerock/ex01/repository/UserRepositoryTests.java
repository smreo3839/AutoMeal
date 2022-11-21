package org.zerock.ex01.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.User;


@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomRecipeRepository customRecipeRepository;

    @Test
    public void testClass() {
        System.out.println(userRepository.getClass().getName());
        for (int i = 0; i < 100; i++) {
            User user = User.builder().userEmail("test" + i + "@gmail.com").nickName("USER"+i).build();
            userRepository.save(user);
        }

    }


    @Test
    void testPageDefault() {
        Pageable pagrab = PageRequest.of(0, 3);
        Page<User> Result = userRepository.findAll(pagrab);
        System.out.println("정보 : " + Result.getTotalPages());
    }
}
