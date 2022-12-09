package org.zerock.ex01.entity;

import lombok.*;

import javax.persistence.*;

@Entity

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "mealPlan")
public class MealPlanList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto imcrement
    private Long planListId;//AUTO_PK
    private Long recipeId;//레시피 아이디
    private String imageType;
    private String title;//레시피 제목
    private int readyInMinutes;//조리시간
    private int servings;//레시피 양
    private boolean clearState;// 레시피 조리 완료 여부
    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩 방식 추구 이유는 데이터를 한꺼번에 조회함으로 즉시로딩 상용시 성능문제가 발생 할 수 있음//일대일 단방향 매핑
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;

    public void resetClearState() {
        this.clearState = false;
    }
}
