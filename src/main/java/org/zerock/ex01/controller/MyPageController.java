package org.zerock.ex01.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex01.dto.CartDetailDTO;
import org.zerock.ex01.dto.ResponseDTO;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.service.CartService;
import org.zerock.ex01.service.UserService;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("profile")
public class MyPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;


    @PostMapping
    public ResponseEntity<?> myProfile(@AuthenticationPrincipal String userId) {
        log.info("마이페이지 called");
        User user = userService.getByCredentials(userId);
        if (user != null) {
            final User responseUserDTO = User.builder()
                    .userEmail(user.getUserEmail())
                    .nickName(user.getNickName())
                    .address(user.getAddress())
                    .img(user.getImg())
                    .diet(user.getDiet())
                    .phoneNum(user.getPhoneNum())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("마이페이지 에러")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/successCheckPhonNumber")
    public ResponseEntity<?> phoneNumberWithApi(@AuthenticationPrincipal String userId, @RequestBody Map<String, Object> rsMap) throws IOException, ParseException {
        log.info("successCheck!!");
        log.info(userId);
        User user = userService.getByCredentials(userId);
        user.setPhoneNum(userService.phoneNumberWithApi(String.valueOf(rsMap.get("imp_uid"))).get("phone_num"));
        return ResponseEntity.ok().body(user);

    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal String userId, @RequestBody UserDTO userDTO) {
        log.info("updateProfile 호출");
        User user = User.builder()
                .phoneNum(userDTO.getPhoneNum())
                .address(userDTO.getAddress())
                .img(userDTO.getImg())
                .nickName(userDTO.getNickName())
                .diet(userDTO.getDiet())
                .build();
        try {
            User responseUser = userService.updateUser(user, userId);
            return ResponseEntity.ok(responseUser);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/diet")
    public ResponseEntity<?> diet(@AuthenticationPrincipal String userId, @RequestParam String diet) {
        log.info("식성 수정 혹은 설정하기");
        try {
            User responseUser = userService.setDiet(diet, userId);
            return ResponseEntity.ok(responseUser);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/cartHist")//장바구니 모아보기
    public ResponseEntity<?> cartHist(@AuthenticationPrincipal String userId) {
        log.info("장바구니 모아보기");
        List<CartDetailDTO> cartDetailList = null;
        ResponseDTO responseDTO = null;
        try {
            cartDetailList = cartService.getCartList(userId);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return ResponseEntity.ok().body(cartDetailList);
    }
}
