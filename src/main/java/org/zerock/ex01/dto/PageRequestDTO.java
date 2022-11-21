package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {//페이지 처리 클래스
    private int page;
    private int size;
    private String type;
    private String keyword;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {//JPA에서 사용하는 Pageable 타입의 객체를 생성
        //PageRequest 객체는 페이지가 0부터 시작하기에 -1 연산
        return PageRequest.of(page - 1, size, sort);//0부터 시작하는 페이지 정보,개수,정렬 조건
    }
}
