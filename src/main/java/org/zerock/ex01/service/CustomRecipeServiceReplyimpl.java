package org.zerock.ex01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.CustomRecipeReplyDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.CustomRecipeReply;
import org.zerock.ex01.repository.CustomRecipeReplyRepository;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class CustomRecipeServiceReplyimpl implements CustomRecipeServiceReply {
    private final CustomRecipeReplyRepository repository;

    @Override
    public Slice<CustomRecipeReply> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("rp_num").descending());//정렬 기준을 매개변수 값으로 넘기고 pageRequest 객체 생성
        Slice<CustomRecipeReply> result = repository.selectCustomRecipeReplyBySlice(requestDTO.getCsRecipeId(), pageable);
        //Function<CustomRecipeReply, CustomRecipeReplyDTO> fn = (entity -> entityToDto(entity));

        return result;
    }

    @Override
    public Long register(String userId, CustomRecipeReplyDTO dto) {
        log.info("DTO");
        CustomRecipeReply entity = dtoToEntity(userId, dto);//dto를 entity타입으로 변환
        log.info(entity);
        repository.save(entity);
        return entity.getRp_num();
    }
}
