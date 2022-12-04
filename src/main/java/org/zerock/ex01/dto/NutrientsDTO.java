package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NutrientsDTO {
    private int calories;//칼로리
    private int carbohydrates;//탄수화물
    private int fat;//지방
    private int protein;//단백질
}
