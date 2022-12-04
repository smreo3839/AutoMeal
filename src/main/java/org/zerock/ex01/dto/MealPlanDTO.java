package org.zerock.ex01.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealPlanDTO {
    private Long id;//PK
    private String imageType;
    private String title;//레시피 제목
    private int readyInMinutes;//조리시간
    private int servings;//레시피 양
    private boolean clear_state;
    private Long recipeId;
}
