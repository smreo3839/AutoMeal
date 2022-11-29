package org.zerock.ex01.dto;


import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
//@Builder
public class OrderDTO {
    @NotNull(message="상품아이디는 필수 입력 값입니다.")
    private Long productId;

    @Min(value = 1,message="최소 주문 수량은 1개입니다.")
    @Max(value = 1,message="최대 주문 수량은 999개 입니다.")
    private int count;

    public OrderDTO() {}

}
