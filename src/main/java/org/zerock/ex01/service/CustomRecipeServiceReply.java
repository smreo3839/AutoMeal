package org.zerock.ex01.service;

import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.*;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.CustomRecipeReply;
import org.zerock.ex01.entity.User;

import java.util.Map;

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

        UserDTO replyer = UserDTO.builder().userEmail(customRecipeReply.getReplyer().getUserEmail()).nickName(customRecipeReply.getReplyer().getNickName()).img(customRecipeReply.getReplyer().getImg()).build();
        CustomRecipeReplyDTO dto = CustomRecipeReplyDTO.builder()
                .rp_num(customRecipeReply.getRp_num())
                .rp_content(customRecipeReply.getRp_content())
                .imgUrl(customRecipeReply.getImgUrl())
                .writer(replyer)
                .csRecipeId(customRecipeReply.getCustomRecipe().getCsRecipeId())
                .build();
        return dto;
    }

    Map getList(PageRequestDTO requestDTO);

    Long register(String userId, CustomRecipeReplyDTO dto);

    void removeWithReplies(CustomRecipeReplyDTO dto);


    CustomRecipeReplyDTO get(CustomRecipeReplyDTO dto);
}
