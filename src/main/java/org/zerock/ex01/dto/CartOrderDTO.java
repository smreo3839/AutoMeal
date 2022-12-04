package org.zerock.ex01.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDTO {
    private Long cartItemId;

    private List<CartOrderDTO> cartOrderDtoList;
    private String imp_uid;//아임포트(결제서비스 api) pk
    private String preceipt_url;//결제 영수증 url

}
