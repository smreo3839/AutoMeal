package org.zerock.ex01.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.ex01.dto.ResponseDTO;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.security.TokenProvider;
import org.zerock.ex01.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        log.info("회원가입 ");
        try {
            if (userDTO == null || userDTO.getUserEmail() == null) {
                throw new RuntimeException("Useremail null");
            }
            //유저 만들기
            User user = User.builder()
                    .userEmail(userDTO.getUserEmail())
                    .phoneNum(userDTO.getPhoneNum())
                    .address(userDTO.getAddress())
                    .img(userDTO.getImg())
                    .nickName(userDTO.getNickName())
                    .build();
            //서비스 이용해 레포지터리에 유저 저장
            User registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .userEmail(registeredUser.getUserEmail())
                    .nickName(registeredUser.getNickName())
                    .build();

            return ResponseEntity.ok(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/recipeBookMarkList")
    public Map<String, Object> recipeBookMarkList(@AuthenticationPrincipal String userId) {
        log.info("recipeBookMarkList");
        return userService.findAllBookMark(userId);
    }
    @PostMapping("/ChangeRecipeDone")
    public Map<String, Object> recipeChangeRecipeDone(@AuthenticationPrincipal String userId) {
        log.info("recipeBookMarkList");
        return userService.findAllChangeRecipeDone(userId);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        User user = userService.getByCredentials(userDTO.getUserEmail());
        if (user != null) {
            //토큰 생성
            final String token = tokenProvider.create(user);
            final User responseUserDTO = User.builder()
                    .userEmail(user.getUserEmail())
                    .nickName(user.getNickName())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login Failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
