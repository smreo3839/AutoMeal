package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.MealPlan;

public interface MealPlanRepostiory extends JpaRepository<MealPlan, Long> {
}





