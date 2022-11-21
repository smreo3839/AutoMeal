package org.zerock.ex01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.RecipeBookMarkDTO;
import org.zerock.ex01.entity.RecipeBookMark;
import org.zerock.ex01.repository.RecipeBookMarkRepository;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class MainRecipeServiceImpl implements MainRecipeService {

    private final RecipeBookMarkRepository recipeBookMarkRepository;

    @Override
    public void ChangeBookmark(RecipeBookMarkDTO dto) {
        log.info("ChangeBookmark");
        RecipeBookMark entity = getBookMarkInfo(dto);
        if (entity == null) {//새로 등록한 북마크라면
            entity = dtoToEntity(dto);
        } else if (!entity.isRecipeDone()) {//기존에 있던 북마크이면서 book_mark,recipe_done 필드가 둘다 null일 경우
            recipeBookMarkRepository.deleteById(entity.getBmNum());
            return;
        }
        entity.setBook_mark(!entity.isBook_mark());//상태 변환
        recipeBookMarkRepository.save(entity);
    }

    @Override
    public void ChangeRecipeDone(RecipeBookMarkDTO dto) {
        log.info("ChangeRecipeDone");
        RecipeBookMark entity = getBookMarkInfo(dto);
        if (entity == null) {//새로 등록한 북마크라면
            entity = dtoToEntity(dto);
        } else if (!entity.isBook_mark()) {//기존에 있던 북마크이면서 book_mark,recipe_done 필드가 둘다 null일 경우
            recipeBookMarkRepository.deleteById(entity.getBmNum());
            return;
        }
        entity.setRecipeDone(!entity.isRecipeDone());//상태 변환
        recipeBookMarkRepository.save(entity);
    }

    @Override
    public RecipeBookMark getBookMarkInfo(RecipeBookMarkDTO dto) {
        RecipeBookMark entity = recipeBookMarkRepository.getBookMark(dto.getRecipe_id(), dto.getUser_email());
        return entity;
    }

}
