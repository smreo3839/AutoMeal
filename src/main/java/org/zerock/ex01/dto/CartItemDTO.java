package org.zerock.ex01.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartItemDTO {

    @NotNull(message="상품아이디는 필수 입력값입니다")
    private Long productId;

    @Min(value=1,message="최소 1개이상담아주세요")
    private int count;
}
