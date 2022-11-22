package org.zerock.ex01.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex01.dto.OrderDTO;
import org.zerock.ex01.dto.OrderHistDTO;
import org.zerock.ex01.dto.ResponseDTO;
import org.zerock.ex01.entity.Order;
import org.zerock.ex01.service.OrderService;

import java.util.Optional;

@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> order(@AuthenticationPrincipal String userId,@RequestBody OrderDTO orderDto){
        log.info("주문하기 생성");
        Long orderId;
        try {
            orderId = orderService.order(orderDto, userId);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping (value = {"/order","/order/{page}"})
    public ResponseEntity<?> orderHist(@PathVariable("page")Optional<Integer> page, @AuthenticationPrincipal String userId, Model model){
        log.info("구매이력 조회");

        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,4);//한번에 주문 개수를 4개로 제한
        Page<OrderHistDTO> ordersHistDtoList=orderService.getOrderList(userId,pageable);

        model.addAttribute("orders",ordersHistDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return ResponseEntity.ok().body(model);
    }

    @PostMapping("/order/cancel")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal String userId,@RequestParam Long orderId){
        if(!orderService.validateOrder(orderId,userId)){
            ResponseDTO responseDTO=ResponseDTO.builder()
                    .error("주문 취소 권한이 없습니다")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().body(orderId);
    }





}
