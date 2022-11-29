package org.zerock.ex01.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex01.dto.*;
import org.zerock.ex01.entity.Order;
import org.zerock.ex01.service.CartService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping//장바구니 만들기
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

    @GetMapping//장바구니 모아보기
    public ResponseEntity<?> orderHist(@AuthenticationPrincipal String userId){
        List<CartDetailDTO> cartDetailList;
        try{
            cartDetailList=cartService.getCartList(userId);
        }catch (Exception e){
            ResponseDTO responseDTO= ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return ResponseEntity.ok().body(cartDetailList);
    }

    @PutMapping("/updateCartItem")//장바구니 수정하기
    public ResponseEntity<?> updateCartItem(@AuthenticationPrincipal String userId,@RequestBody CartDetailDTO cartDetailDto){
        ResponseDTO responseDTO;
        if(cartDetailDto.getCount() <=0){
            responseDTO= ResponseDTO.builder().error("최소 1개이상 담아주세요").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }else if(!cartService.validateCartItem(cartDetailDto.getCartItemId(), userId)){
            responseDTO= ResponseDTO.builder().error("수정권한이 없습니다").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        cartService.updateCartItemCount(cartDetailDto.getCartItemId(), cartDetailDto.getCount());
        return ResponseEntity.ok().body(cartDetailDto);
    }

    @DeleteMapping("/deleteCartItem")
    public ResponseEntity<?> deleteCartItem(@AuthenticationPrincipal String userId,@RequestBody CartDetailDTO cartDetailDto){
        ResponseDTO responseDTO;
        if(!cartService.validateCartItem(cartDetailDto.getCartItemId(), userId)){
            responseDTO= ResponseDTO.builder().error("수정권한이 없습니다").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        cartService.deleteCartItem(cartDetailDto.getCartItemId());
        return ResponseEntity.ok().body(cartDetailDto);
    }

    @PostMapping("/orderCartItem")
    public ResponseEntity<?> orderCartItem(@AuthenticationPrincipal String userId, @RequestBody CartOrderDTO cartOrderDTO){
        List<CartOrderDTO> cartOrderDTOList=cartOrderDTO.getCartOrderDtoList();
        ResponseDTO responseDTO;
        if(cartOrderDTOList ==null || cartOrderDTOList.size() ==0){//주문할 상품을 선택하지 않았는지 체크
            responseDTO= ResponseDTO.builder().error("주문할 상품을 선택해주세요").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        for(CartOrderDTO cartOrder: cartOrderDTOList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), userId)){
                responseDTO= ResponseDTO.builder().error("주문 권한이 없습니다").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        }
        Order order=cartService.orderCartItem(cartOrderDTOList,userId);//주문로직 호출 결과 생성된 주문번호를 반환받음
        return ResponseEntity.ok().body(order);
    }

}
