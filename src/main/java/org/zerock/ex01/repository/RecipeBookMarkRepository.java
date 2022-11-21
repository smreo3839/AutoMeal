package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.ex01.entity.RecipeBookMark;

import java.util.List;

public interface RecipeBookMarkRepository extends JpaRepository<RecipeBookMark, Long> {
    @Query("SELECT rbm FROM RecipeBookMark rbm WHERE rbm.recipe_id =:recipe_id and rbm.user.user_email =:user_email")
    RecipeBookMark getBookMark(String recipe_id, String user_email);//Entitu 타입 정보와 기본키의 자료형 지정

    @Query("SELECT rbm.recipe_id FROM RecipeBookMark rbm WHERE rbm.user.user_email =:user_email and rbm.book_mark=true")
    List<String> findAllBookMark(String user_email);//Entitu 타입 정보와 기본키의 자료형 지정
    //인터페이스 선언만으로 자동으로 스프링 빈으로 등록

}
