package org.zerock.ex01.service;

import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.CustomRecipeReplyDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.CustomRecipeReply;
import org.zerock.ex01.entity.User;

public interface CustomRecipeServiceReply {
    //서비스 계층에서는 파라미터를 DTO 타입으로 받기에 JPA로 처리하기 위해서는 ENTITY타입의 객체롭 변환시켜야한다.


    default CustomRecipeReply dtoToEntity(String userId, CustomRecipeReplyDTO dto) {
        User user = User.builder().userEmail(userId).build();
        CustomRecipe customRecipe = CustomRecipe.builder().csRecipeId(dto.getCsRecipeId()).build();
        CustomRecipeReply entity = CustomRecipeReply.builder()
                .rp_content(dto.getRp_content())
                .imgUrl(dto.getImgUrl())
                .customRecipe(customRecipe)
                .replyer(user)
                .build();
        return entity;
    }

    default CustomRecipeReplyDTO entityToDto(CustomRecipeReply customRecipeReply) {


        CustomRecipeReplyDTO dto = CustomRecipeReplyDTO.builder()
                .rp_num(customRecipeReply.getRp_num())
                .rp_content(customRecipeReply.getRp_content())
                .imgUrl(customRecipeReply.getImgUrl())
                .writerEmail(customRecipeReply.getReplyer().getUserEmail())
                .writerNickName(customRecipeReply.getReplyer().getNickName())
                .csRecipeId(customRecipeReply.getCustomRecipe().getCsRecipeId())
                .build();
        return dto;
    }

    Slice<CustomRecipeReply> getList(PageRequestDTO requestDTO);

    Long register(String userId, CustomRecipeReplyDTO dto);
}
