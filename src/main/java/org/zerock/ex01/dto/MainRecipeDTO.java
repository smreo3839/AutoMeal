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
    private String excludeIngredients;
    private String includeIngredients;
    private String equipment;
    private String intolerances;
    private String diet;
    private String cuisine;
    private String excludeCuisine;
    //    @Setter(AccessLevel.PROTECTED)
    private int offset=1;
    private int number = 10;


//    public void setOffset(int offset) {
//        this.offset = (offset - 1) * this.number;
//    }

    public int getOffset() {
        return (this.offset - 1) * this.number;
    }

    public Map<String, Object> makePageList(int total) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", offset);
        int totalPage = total / number;

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
