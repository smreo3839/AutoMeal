package org.zerock.ex01.service;

import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.RecipeBookMarkDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.RecipeBookMark;
import org.zerock.ex01.entity.User;

public interface MainRecipeService {
    default RecipeBookMark dtoToEntity(RecipeBookMarkDTO dto) {
        User user = User.builder().user_email(dto.getUser_email()).build();
        RecipeBookMark entity = RecipeBookMark.builder().recipe_id(dto.getRecipe_id()).user(user).recipeDone(dto.isRecipe_done()).book_mark(dto.isBook_mark()).build();
        return entity;
    }

    default CustomRecipeDTO entityToDto(CustomRecipe customRecipe, User user, Long replyCount) {

        CustomRecipeDTO dto = CustomRecipeDTO.builder()
                .csRecipeId(customRecipe.getCsRecipeId())
                .user_email(user.getUser_email())
                .nick_name(user.getNick_name())
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


    void ChangeBookmark(RecipeBookMarkDTO dto);

    void ChangeRecipeDone(RecipeBookMarkDTO dto);

    RecipeBookMark getBookMarkInfo(RecipeBookMarkDTO dto);
}
