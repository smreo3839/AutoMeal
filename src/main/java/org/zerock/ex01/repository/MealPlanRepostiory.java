package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.ex01.entity.MealPlan;

import java.util.List;

public interface MealPlanRepostiory extends JpaRepository<MealPlan, Long> {

    @Query("SELECT M FROM MealPlan M where M.user.userEmail =:userEmail")
    List<MealPlan> findByUserEmail(String userEmail);

    @Query("SELECT count (M) FROM MealPlan M where M.user.userEmail =:userEmail")
    Long countForUserEmail(String userEmail);
}





