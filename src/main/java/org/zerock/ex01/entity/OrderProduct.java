package org.zerock.ex01.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderProduct {

    @Id
    @GeneratedValue
    @Column(name ="order_product_id")//주문 상품 번호
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩 방식 추구 이유는 데이터를 한꺼번에 조회함으로 즉시로딩 상용시 성능문제가 발생 할 수 있음
    @JoinColumn(name="product_id") //하나의 상품은 여러 주문 상품으로 들어 갈 수 있으므로 주문 상품 기준 다대일 단방향 매핑
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩 방식 추구 이유는 데이터를 한꺼번에 조회함으로 즉시로딩 상용시 성능문제가 발생 할 수 있음
    @JoinColumn(name="order_id")//한번의 주문에 여러개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를 다대일 단방향 매핑
    private Order order;

    private int orderPrice;// 주문 가격

    private int count;//수량

    public static OrderProduct createOrderProduct(ProductEntity product,int count){
        OrderProduct orderProduct=new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setCount(count);
        orderProduct.setOrderPrice(product.getPrice());
        return orderProduct;
    }


    public int getTotalPrice() {
        return orderPrice*count;
    }

}
