package org.zerock.ex01.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.zerock.ex01.entity.*;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
public class SlicePagingImpl extends QuerydslRepositorySupport implements SlicePaging {
    public SlicePagingImpl(JPAQueryFactory queryFactory) {
        super(CustomRecipeReply.class);
        this.queryFactory = queryFactory;
    }


    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<CustomRecipeReply> selectCustomRecipeReplyBySlice(Long csRecipeId, Pageable pageable) {
        //JPAQueryFactory 인터페이스 사용 방법
        log.info("selectCustomRecipeReplyBySlice.............................");
        QCustomRecipeReply Qreply = QCustomRecipeReply.customRecipeReply;


        List<CustomRecipeReply> foundQreplyList = queryFactory
                .selectFrom(Qreply)
                .where(Qreply.customRecipe.csRecipeId.eq(csRecipeId))
                .orderBy(Qreply.rp_num.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // limit보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true를 넣어 알림
                .fetch();

        List<CustomRecipeReply> content = new ArrayList<>();
        for (CustomRecipeReply eachCustomRecipeReply : foundQreplyList) {
            content.add(eachCustomRecipeReply);

        }

        boolean hasNext = isContentSizeGreaterThanPageSize(content, pageable);
        return new SliceImpl<>(hasNext ? subListLastContent(content, pageable) : content, pageable, hasNext);
    }

    // 다음 페이지 있는지 확인
    private static <T> boolean isContentSizeGreaterThanPageSize(List<T> content, Pageable pageable) {
        return pageable.isPaged() && content.size() > pageable.getPageSize();
    }

    // 데이터 1개 빼고 반환
    private static <T> List<T> subListLastContent(List<T> content, Pageable pageable) {
        return content.subList(0, pageable.getPageSize());
    }
}
