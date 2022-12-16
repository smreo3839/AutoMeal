package org.zerock.ex01.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.RecipeBookMarkDTO;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.entity.RecipeBookMark;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.RecipeBookMarkRepository;
import org.zerock.ex01.repository.UserRepository;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PayMentManager payMentManager;
    @Autowired
    private RecipeBookMarkRepository recipeBookMarkRepository;

    //회원 생성하기
    public User create(final User userEntity) {
        if (userEntity == null || userEntity.getUserEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String userEmail = userEntity.getUserEmail();

        if (userRepository.existsByUserEmail(userEmail)) {
            log.warn("Username already exists {}", userEmail);
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(userEntity);
    }

    public User getByCredentials(final String userEmail) {
        final User originalUser = userRepository.findByUserEmail(userEmail);
        log.info("로그인해볼게용");
        if (originalUser != null) {
            return originalUser;
        }
        return null;
    }

    public User updateUser(final User userEntity, final String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        log.info("user:{}", userEntity);
        user.updateUser(userEntity);
        userRepository.save(user);
        return user;
    }

    public User setDiet(final String diet, final String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        user.changeDiet(diet);
        userRepository.save(user);
        return user;
    }

    public Map<String, String> phoneNumberWithApi(String imp_uid) throws IOException, ParseException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone_num", payMentManager.CertificationCheck(payMentManager.getToken02(), imp_uid));
        return map;
    }

    public Map<String, Object> findAllBookMark(String userId) {
        return Collections.singletonMap("BookMarkList", recipeBookMarkRepository.findAllBookMark(userId).stream().map(entity -> bookMarkEntityToDto(entity)).collect(Collectors.toList()));
    }

    public Map<String, Object> findAllChangeRecipeDone(String userId) {
        return Collections.singletonMap("BookMarkList", recipeBookMarkRepository.findAllRecipeDone(userId).stream().map(entity -> bookMarkEntityToDto(entity)).collect(Collectors.toList()));
    }

    public RecipeBookMarkDTO bookMarkEntityToDto(RecipeBookMark entity) {
        RecipeBookMarkDTO dto = RecipeBookMarkDTO.builder()
                .bmNum(entity.getBmNum())
                .recipe_id(entity.getRecipe_id())
                .book_mark(entity.isBook_mark())
                .recipe_done(entity.isRecipeDone())
                .user_email(entity.getUser().getUserEmail())
                .recipe_title(entity.getRecipe_title())
                .recipe_thumbnail(entity.getRecipe_thumbnail())
                .build();
        return dto;
    }
}
