package org.zerock.ex01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.ex01.entity.*;


import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(CustomRecipe.class);
    }

    @Override
    public CustomRecipe search1() {

        log.info("search1........................");

        QCustomRecipe customRecipe = QCustomRecipe.customRecipe;
        QCustomRecipeReply reply = QCustomRecipeReply.customRecipeReply;
        QUser user = QUser.user;
        QFoodImage foodImage = QFoodImage.foodImage;

        JPQLQuery<CustomRecipe> jpqlQuery = from(customRecipe);
        jpqlQuery.leftJoin(user).on(customRecipe.writer.eq(user));//(CustomRecipe entity의 fk = user entity의 pk) 조건의 left join 연산
        jpqlQuery.leftJoin(reply).on(reply.custom_recipe.eq(customRecipe));//(CustomRecipeReply entity의 fk = CustomRecipe entity의 pk) 조건의 left join 연산


        JPQLQuery<Tuple> tuple = jpqlQuery.select(customRecipe, user.userEmail, reply.count());
        tuple.groupBy(customRecipe);

        log.info("---------------------------");
        log.info(tuple);
        log.info("---------------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage.............................");

        QCustomRecipe customRecipe = QCustomRecipe.customRecipe;
        QCustomRecipeReply reply = QCustomRecipeReply.customRecipeReply;
        QUser user = QUser.user;

        JPQLQuery<CustomRecipe> jpqlQuery = from(customRecipe);
        jpqlQuery.leftJoin(user).on(customRecipe.writer.eq(user));
        jpqlQuery.leftJoin(reply).on(reply.custom_recipe.eq(customRecipe));
        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.customRecipe = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(customRecipe, user, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();//where절의 조건을 붙일수있는 객체 생성
        //BooleanExpression expression = customRecipe.csRecipeId.gt(0L);//where 조건문 생성

        // booleanBuilder.and(expression);//and 메서드로 where 조건 추가
        booleanBuilder.and(customRecipe.csRecipeId.gt(0L));//and 메서드로 where 조건 추가

        if (type != null) {
            String[] typeArr = type.split("");
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {//동적 쿼리
                switch (t) {
                    case "t":
                        conditionBuilder.or(customRecipe.recipeT_title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(user.nickName.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(customRecipe.recipe_content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        //변경사항
        tuple.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        // tuple.orderBy(customRecipe.csRecipeId.desc());


        // 정렬 기준이 여러개일 떄는 forEach문으로 구현
        sort.stream().forEach(order -> {//여러개의 sort 객체를 연결 할 수 있기에 반복문
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(CustomRecipe.class, "customRecipe");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });
        tuple.groupBy(customRecipe);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());
        log.info("searchPageMkc---------------------------");
        log.info(tuple);
        log.info("searchPageMkc---------------------------");
        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " + count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count);
    }
}
