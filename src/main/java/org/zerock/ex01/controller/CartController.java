package org.zerock.ex01.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.ex01.dto.CartItemDTO;
import org.zerock.ex01.dto.ResponseDTO;
import org.zerock.ex01.service.CartService;

@RestController
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/ ")
    public ResponseEntity<?> order(@RequestBody CartItemDTO cartItemDTO, @AuthenticationPrincipal String userId){
        Long cartItemId;
        try{
            cartItemId=cartService.addCart(cartItemDTO,userId);
        }catch (Exception e){
            ResponseDTO responseDTO= ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return ResponseEntity.ok().body(cartItemId);
    }

}
