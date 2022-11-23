package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartDetailDTO {

    private Long cartItemId; //장바구니 상품 아이디

    private String productNm;//상품명

    private int price;//상품 금액

    private int count;// 수량

    private String imgUrl;
}
