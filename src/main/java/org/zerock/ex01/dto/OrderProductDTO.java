package org.zerock.ex01.dto;

import lombok.Getter;
import lombok.Setter;
import org.zerock.ex01.entity.OrderProduct;

@Getter
@Setter
public class OrderProductDTO {//구매이력

    public OrderProductDTO(OrderProduct orderProduct){
        this.productNm=orderProduct.getProduct().getProductName();
        this.count=orderProduct.getCount();
        this.orderPrice=orderProduct.getOrderPrice();
        this.imgUrl=orderProduct.getProduct().getImageFirst();
    }

    private String productNm; //상품명

    private int count; //주문 수량

    private int orderPrice;// 주문 금액

    private String imgUrl;// 상품 이미지 경로0.

}
