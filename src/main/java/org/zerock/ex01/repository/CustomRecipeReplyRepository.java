package org.zerock.ex01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex01.entity.CustomRecipeReply;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.qdsl.SlicePaging;

import java.util.List;

public interface CustomRecipeReplyRepository extends JpaRepository<CustomRecipeReply, Long>, SlicePaging {
    @Modifying
    @Query("delete from CustomRecipeReply r where r.customRecipe.csRecipeId =:csRecipeId ")
    void deleteByBno(@Param("csRecipeId") Long csRecipeId);

    @Query("select r from  CustomRecipeReply r where r.customRecipe.csRecipeId =:csRecipeId ")
    Page<CustomRecipeReply> findByPageToCsRecipeId(Pageable pageable, Long csRecipeId);
}
