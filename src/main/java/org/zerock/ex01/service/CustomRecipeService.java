package org.zerock.ex01.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.User;

public interface CustomRecipeService {
    //서비스 계층에서는 파라미터를 DTO 타입으로 받기에 JPA로 처리하기 위해서는 ENTITY타입의 객체롭 변환시켜야한다.


    default CustomRecipe dtoToEntity(CustomRecipeDTO dto) {
        User user = User.builder().userEmail(dto.getUser_email()).build();
        CustomRecipe entity = CustomRecipe.builder()
                .csRecipeId(dto.getCsRecipeId())
                .writer(user)
                .recipeT_title(dto.getRecipeT_title())
                .recipe_content(dto.getRecipe_content())
                .like_rate(dto.getLike_rate())
                .recipe_category(dto.getRecipe_category())
                .cr_hits((dto.getCr_hits()))
                .build();
        return entity;
    }

    default CustomRecipeDTO entityToDto(CustomRecipe customRecipe, User user, Long replyCount) {

        CustomRecipeDTO dto = CustomRecipeDTO.builder()
                .csRecipeId(customRecipe.getCsRecipeId())
                .user_email(user.getUserEmail())
                .nick_name(user.getNickName())
                .recipeT_title(customRecipe.getRecipeT_title())
                .recipe_content(customRecipe.getRecipe_content())
                .regDate(customRecipe.getRegDate())
                .modDate(customRecipe.getModDate())
                .like_rate(customRecipe.getLike_rate())
                .recipe_category(customRecipe.getRecipe_category())
                .cr_hits(customRecipe.getCr_hits())
                .replyCount(replyCount.intValue())//Long 자료형을 int로 변환
                .build();
        return dto;
    }

    Long register(CustomRecipeDTO dto);

    PageResultDTO<CustomRecipeDTO, Object[]> getList(PageRequestDTO requestDTO);

    CustomRecipeDTO get(Long csRecipeId);


    @Transactional
    void removeWithReplies(Long bno);

    Long modify(CustomRecipeDTO dto);
}
