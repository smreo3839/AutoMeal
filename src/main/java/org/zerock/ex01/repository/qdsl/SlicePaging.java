package org.zerock.ex01.repository.qdsl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.zerock.ex01.entity.CustomRecipeReply;

public interface SlicePaging {
    Slice<CustomRecipeReply> selectCustomRecipeReplyBySlice(Long csRecipeId, Pageable pageable);
}
