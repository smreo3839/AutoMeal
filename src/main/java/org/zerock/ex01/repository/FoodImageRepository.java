package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.entity.FoodImage;

import java.util.List;

public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {
    @Query("SELECT f FROM FoodImage f WHERE  f.custom_recipe.csRecipeId =:csRecipeId")
    List<FoodImage> getImgList(Long csRecipeId);

    @Modifying
    @Transactional
    @Query("delete from FoodImage f where f.custom_recipe.csRecipeId =:csRecipeId")
    void delImgList(Long csRecipeId);


}
