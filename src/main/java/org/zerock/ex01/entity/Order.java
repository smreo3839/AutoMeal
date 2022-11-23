package org.zerock.ex01.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.zerock.ex01.constant.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="order_history")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩 방식 추구 이유는 데이터를 한꺼번에 조회함으로 즉시로딩 상용시 성능문제가 발생 할 수 있음
    @JoinColumn(name="user_email")//한명의 회원은 여러번 주문할 수 있으니 다대일 단방향 매핑
    private User user;

    private LocalDateTime orderDate;//주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;//주문 상태

    //부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CasscadeType옵션
    @JsonIgnore
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)//OrderProduct에 있는 order에 의해 관리 된다는 의미 +고아객체 제거하기
    private List<OrderProduct> orderProducts =new ArrayList<>();//하나의 주문이 여러개의 주ㅡ문 상품을 갖으므로 list형 자료

    public void addOrderItem(OrderProduct orderProduct){
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    //상품 주문한 회원의 정보를 세팅
    public static Order createOrder(User user,List<OrderProduct> orderProductList){
        Order order=new Order();
        order.setUser(user);
        for(OrderProduct orderProduct : orderProductList){
            order.addOrderItem(orderProduct);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice(){
        int totalPrice =0;
        for(OrderProduct orderProduct: orderProducts){
            totalPrice+= orderProduct.getTotalPrice();
        }
        return  totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus=OrderStatus.CANCEL;
    }



}
