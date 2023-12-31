package org.zerock.ex01.dto;

import lombok.Getter;
import lombok.Setter;
import org.zerock.ex01.constant.OrderStatus;
import org.zerock.ex01.entity.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDTO { // 주문 정보를 담을 OrderHistDTO

    public OrderHistDTO(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        this.imp_uid = order.getImp_uid();
        this.preceipt_url = order.getPreceipt_url();
    }

    private Long orderId;

    private String orderDate; //주문날짜

    private OrderStatus orderStatus;// 주문상태
    private String imp_uid;//아임포트(결제서비스 api) pk
    private String preceipt_url;//결제 영수증 url


    //주문 상품 리스트
    private List<OrderProductDTO> orderProductDtoList = new ArrayList<>();


    //객체를 주문 상품 리스트에 추가하는 메소드
    public void addOrderProductDto(OrderProductDTO orderProductDto) {
        orderProductDtoList.add(orderProductDto);
    }


}
