package org.zerock.ex01.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MealPlanList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto imcrement
    private Long planListId;//AUTO_PK
    private Long recipeId;
    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩 방식 추구 이유는 데이터를 한꺼번에 조회함으로 즉시로딩 상용시 성능문제가 발생 할 수 있음//일대일 단방향 매핑
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;
}
