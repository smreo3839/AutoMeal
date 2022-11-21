package org.zerock.ex01.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    /*JPA를 이용하는 Repostiory에서는 페이지 처리 결과를 Page 처리 결거ㅏ를 Page<Entity>타입으로 반환하기에
     서비스 계층에서 처리해주어야하는 부분은
    1. Page<Entity>의 엔티티 객체들을 DTO 객체로 변환해서 자료구조로 담어 주어야합니다
    2. 화면 출력에 필요한 페이지 정보들을 구성해 주어야 합니다*/
    //DTO리스트
    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    //현재 페이지 번호
    private int page;
    //목록 사이즈
    private int size;

    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    //이전, 다음
    private boolean prev, next;
    //페이지 번호 목록
    private List<Integer> pageList;


    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {//Page<Entity>를 List<DTO>로 변환하는 메서드
        //어느곳에서 사용할 수 있도록 제네릭 타입을 이용해서 DTO와 EN 타입으로 지정
        //Function<EN, DTO>는 엔티티 객체들을 DTO로 변환해주는 기능
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        //stream() => stream을 통해 Entity 꺼내줌
        //map(fn) => 스트림내 요소들에 대해 함수(fn)가 적용된 결과의 새로운 요소로 매핑해준다. entity 를 dto로 매핑
        //collect(Collectors.toList()) => Stream을 List로 변환
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

    }
}
