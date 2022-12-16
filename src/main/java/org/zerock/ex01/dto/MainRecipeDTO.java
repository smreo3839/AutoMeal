package org.zerock.ex01.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@Data
public class MainRecipeDTO {
    private String query;//레시피 검색 키워드
    private String excludeIngredients;//레시피에서 제외할 식재료
    private String includeIngredients;//레시피에서 추가할 식재
    private String equipment;//조리에 필요한 조리도구
    private String intolerances;//알러지 유발 식품정보
    private String diet;//식단 정보(ex. Vegetarian)
    private String cuisine;//국가 별 레시피
    private String excludeCuisine;//해당 국tt가 제외 레시피
    @Builder.Default   //    @Setter(AccessLevel.PROTECTED)
    private int offset = 1;
    @Builder.Default
    private int number = 30;


//    public void setOffset(int offset) {
//        this.offset = (offset - 1) * this.number;
//    }

    public int getOffset() {
        return (this.offset - 1) * this.number;
    }

    public Map<String, Object> makePageList(int total) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", offset);
        int totalPage = (int) (Math.ceil(total / 10.0));

        //temp end page
        int tempEnd = (int) (Math.ceil(offset / 10.0)) * 10;

        int start = tempEnd - 9;

        boolean prev = start > 1;

        int end = totalPage > tempEnd ? tempEnd : totalPage;

        boolean next = totalPage > tempEnd;

        List<Integer> pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        map.put("start", start);
        map.put("end", end);
        map.put("prev", prev);
        map.put("next", next);
        map.put("pageList", pageList);
        return map;
    }
}
