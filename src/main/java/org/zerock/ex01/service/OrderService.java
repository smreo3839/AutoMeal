package org.zerock.ex01.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.OrderDTO;
import org.zerock.ex01.dto.OrderHistDTO;
import org.zerock.ex01.dto.OrderProductDTO;
import org.zerock.ex01.entity.Order;
import org.zerock.ex01.entity.OrderProduct;
import org.zerock.ex01.entity.ProductEntity;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.OrderRepository;
import org.zerock.ex01.repository.ProductRepository;
import org.zerock.ex01.repository.UserRepository;

import javax.persistence.EntityNotFoundException;

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
    private final PayMentManager payMentManager;


    public Long order(OrderDTO orderDto, String email) {
        ProductEntity product = productRepository.findById(orderDto.getProductId()).orElseThrow(EntityNotFoundException::new);//주문 상품조회
        User user = userRepository.findByUserEmail(email);//현재 로그인한 회원의 이메일 정보를 이용해서 훠원 정보를 조회합니다.

        List<OrderProduct> orderProductList = new ArrayList<>();
        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, orderDto.getCount());//주문할 상품 엔티티와 주문수량을 이용하여 주문 상품 엔티티 생성
        orderProductList.add(orderProduct);

        Order order = Order.createOrder(user, orderProductList, orderDto.getImp_uid(), orderDto.getPreceipt_url());//회원 정보와 주문할 상품 리스트 정보를 이용해 주문 엔티티를 생성함
        orderRepository.save(order);//생성한 주문 엔티티를 저장
        log.info("총가격: {}", order.getTotalPrice());
        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDTO> getOrderList(String email, Pageable pageable) {
        log.info("order history");
        List<Order> orders = orderRepository.findOrders(email, pageable);//유저이메일과 페이징 조건 이용 주문 목록 조회
        Long totalCount = orderRepository.countOrder(email);//유저의 주문 총개수 구함

        List<OrderHistDTO> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDTO orderHistDto = new OrderHistDTO(order);
            List<OrderProduct> orderProducts = order.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                OrderProductDTO orderProductDTO = new OrderProductDTO(orderProduct);
                orderHistDto.addOrderProductDto(orderProductDTO);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDTO>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {//현재 로그인 한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사
        User curUser = userRepository.findByUserEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        User savedUser = order.getUser();

        if (!StringUtils.equals(curUser.getUserEmail(), savedUser.getUserEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId) {//주문 취소 상태로 변경하면 변경 감지 기능에 의해서 트랜잭션이 끝날때 update 쿼리 실행
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        payMentManager.canclePay(payMentManager.getToken02(), order.getImp_uid());

        order.cancelOrder();
    }

    public Order orders(List<OrderDTO> orderDTOList, String email, String imp_uid, String preceipt_url) {
        User user = userRepository.findByUserEmail(email);
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (OrderDTO orderDto : orderDTOList) {
            ProductEntity product = productRepository.findByProductId(orderDto.getProductId());

            OrderProduct orderProduct = OrderProduct.createOrderProduct(product, orderDto.getCount());
            orderProductList.add(orderProduct);
        }
        Order order = Order.createOrder(user, orderProductList, imp_uid, preceipt_url);
        return orderRepository.save(order);
    }
}
