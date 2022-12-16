package org.zerock.ex01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.CustomRecipeReplyDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.CustomRecipeReply;
import org.zerock.ex01.repository.CustomRecipeReplyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class CustomRecipeServiceReplyimpl implements CustomRecipeServiceReply {
    private final CustomRecipeReplyRepository repository;

    @Override
    public Map getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("rp_num").descending());//정렬 기준을 매개변수 값으로 넘기고 pageRequest 객체 생성
        Slice<CustomRecipeReply> result = repository.selectCustomRecipeReplyBySlice(requestDTO.getCsRecipeId(), pageable);
        List<CustomRecipeReplyDTO> dtoList = result.getContent().stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
        Map<String, Object> FinalResult = new HashMap<>();
        FinalResult.put("content", dtoList);
        FinalResult.put("pageable", result.getPageable());
        FinalResult.put("sort", result.getSort());
        FinalResult.put("first", result.isFirst());
        FinalResult.put("last", result.isLast());
        FinalResult.put("empty", result.isEmpty());
        //Function<CustomRecipeReply, CustomRecipeReplyDTO> fn = (entity -> entityToDto(entity));

        return FinalResult;
    }

    @Override
    public Long register(String userId, CustomRecipeReplyDTO dto) {
        log.info("DTO");
        CustomRecipeReply entity;
        if (dto.getRp_num() == 0) {
            entity = dtoToEntity(userId, dto);//dto를 entity타입으로 변환
        } else {
            entity = repository.getOne(dto.getRp_num());
            entity.changeRp_content(dto.getRp_content());
        }

        log.info(entity);
        repository.save(entity);
        return entity.getRp_num();
    }

    @Override
    public void removeWithReplies(CustomRecipeReplyDTO dto) {
        repository.deleteById(dto.getRp_num());
    }

    @Override
    public CustomRecipeReplyDTO get(CustomRecipeReplyDTO dto) {
        return entityToDto(repository.findById(dto.getRp_num()).get());
    }


}
