package org.zerock.ex01.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.OrderDTO;
import org.zerock.ex01.entity.Order;
import org.zerock.ex01.entity.OrderProduct;
import org.zerock.ex01.entity.ProductEntity;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.OrderRepository;
import org.zerock.ex01.repository.ProductRepository;
import org.zerock.ex01.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDTO orderDto, String email){
        ProductEntity product=productRepository.findById(orderDto.getProductId()).orElseThrow(EntityNotFoundException::new);//주문 상품조회
        User user=userRepository.findByUserEmail(email);//현재 로그인한 회원의 이메일 정보를 이용해서 훠원 정보를 조회합니다.

        List<OrderProduct> orderProductList=new ArrayList<>();
        OrderProduct orderProduct=OrderProduct.createOrderProduct(product,orderDto.getCount());//주문할 상품 엔티티와 주문수량을 이용하여 주문 상품 엔티티 생성
        orderProductList.add(orderProduct);

        Order order = Order.createOrder(user,orderProductList);//회원 정보와 주문할 상품 리스트 정보를 이용해 주문 엔티티를 생성함
        orderRepository.save(order);//생성한 주문 엔티티를 저장
        return order.getId();
    }

}
