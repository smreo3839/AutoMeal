package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealPlanResulttDTO {
    private List<MealPlanDTO> meals;
    private NutrientsDTO nutrients;
    private Long plan_id;
    private String userEmail;
}
