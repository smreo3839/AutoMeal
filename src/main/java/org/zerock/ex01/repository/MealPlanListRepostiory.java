package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.MealPlan;
import org.zerock.ex01.entity.MealPlanList;

public interface MealPlanListRepostiory extends JpaRepository<MealPlanList, Long> {
}
