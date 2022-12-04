package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.entity.MealPlan;
import org.zerock.ex01.entity.MealPlanList;

import java.util.List;

public interface MealPlanListRepostiory extends JpaRepository<MealPlanList, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE MealPlanList M SET M.clearState = case M.clearState when true then false  ELSE TRUE END WHERE M.planListId =:planListId")
    void changeStatus(Long planListId);
}
