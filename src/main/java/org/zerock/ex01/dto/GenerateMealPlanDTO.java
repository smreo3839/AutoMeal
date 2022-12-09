package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateMealPlanDTO {
    private String timeFrame;//Meal타입(day or week)
    private String targetCalories;//제공받을 레시피들의 총 칼로리
    private String diet;//식단 정보(ex. vegetarian)
    private String exclude;//제외할 알러지 또는 성분
}
