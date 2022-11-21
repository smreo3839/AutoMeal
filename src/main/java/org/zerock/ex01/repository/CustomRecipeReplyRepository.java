package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.ex01.entity.CustomRecipeReply;
import org.zerock.ex01.entity.User;

public interface CustomRecipeReplyRepository extends JpaRepository<CustomRecipeReply, Long> {
    @Modifying
    @Query("delete from CustomRecipeReply r where r.custom_recipe.csRecipeId =:csRecipeId ")
    void deleteByBno(Long csRecipeId);
}
