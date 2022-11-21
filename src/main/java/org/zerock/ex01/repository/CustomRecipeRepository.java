package org.zerock.ex01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.repository.search.SearchBoardRepository;

import java.util.List;

public interface CustomRecipeRepository extends JpaRepository<CustomRecipe, Long>, SearchBoardRepository {//Entitu 타입 정보와 기본키의 자료형 지정

    //인터페이스 선언만으로 자동으로 스프링 빈으로 등록
    @Query("select c, w from CustomRecipe c left join c.writer w where c.csRecipeId =:csRecipeId")
    //연관관계가 있는 테이블 끼리 JOIN할 경우 LEFT JOIN 뒤에 ON을 사용하지 않아도 가능하다
    Object getCustomRecipeWithWriter(@Param("csRecipeId") Long csRecipeId);


    @Query("SELECT C, R FROM CustomRecipe C LEFT JOIN CustomRecipeReply R on C = R.custom_recipe WHERE C.csRecipeId = :csRecipeId")
    List<Object[]> getCustomRecipeWithReply(@Param("csRecipeId") Long csRecipeId);

    //CustomRecipeReply테이블은 CustomRecipe테이블을 참조하나
    // CustomRecipe 테이블은 CustomRecipeReply테이블을 참조 하지 않기 떄문에
    // 연관관계가 없는 테이블 끼리 JOIN할 경우 LEFT JOIN 뒤에 ON을 사용해 조인 조건을 추가해주어야 한다
    @Query(value = "SELECT c, w, count(r) " +
            " FROM CustomRecipe c " +
            " LEFT JOIN c.writer w " +
            " LEFT JOIN CustomRecipeReply r ON r.custom_recipe = c " +
            " GROUP BY c",
            countQuery = "SELECT count(c) FROM CustomRecipe c")
    Page<Object[]> getCustomRecipeReplyCount(Pageable pageable); //게시물 정보,글쓴이 회원 정보, 댓글 수

    @Query("SELECT c, w, count(r) " +
            " FROM CustomRecipe c  LEFT JOIN c.writer w " +
            " LEFT OUTER JOIN CustomRecipeReply r ON r.custom_recipe = c" +
            " WHERE c.csRecipeId = :csRecipeId")
    Object getCustomRecipeByCsRecipeId(@Param("csRecipeId") Long csRecipeId);
}
