package org.zerock.ex01.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.service.CustomRecipeService;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CustomRecipeRepositoryTests {
    @Autowired
    CustomRecipeRepository customRecipeRepository;
    @Autowired
    CustomRecipeService service;

    @Test
    public void testCustomRecipeSave() {//입력 테스트
        System.out.println(customRecipeRepository.getClass().getName());
        for (int i = 0; i < 100; i++) {
            User user = User.builder().userEmail("test" + i + "@gmail.com").build();
            CustomRecipe entity = CustomRecipe.builder().recipeT_title("customRecipe 테스트 제목입니다" + i)
                    .recipe_content("customRecipe 테스트 내용입니다" + i).writer(user).build();
            customRecipeRepository.save(entity);
        }

    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<CustomRecipeDTO, Object[]> resultDTO = service.getList(pageRequestDTO);
        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("-------------------------------------");
        for (CustomRecipeDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("========================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testWithReplyCount() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("csRecipeId").descending());

        Page<Object[]> result = customRecipeRepository.getCustomRecipeReplyCount(pageable);

        result.get().forEach(row -> {

            System.out.println(Arrays.toString((Object[]) row));
        });
    }

    @Test
    public void testReadWithWriter() {

        Object result = customRecipeRepository.getCustomRecipeWithWriter(5L);

        Object[] arr = (Object[]) result;

        System.out.println("-------------------------------");
        System.out.println(Arrays.toString(arr));

    }

    @Test
    public void testGetCustomRecipeWithReply() {

        List<Object[]> result = customRecipeRepository.getCustomRecipeWithReply(5L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testSearch1() {
        customRecipeRepository.search1();
    }

    @Test
    public void testSearchPage() {

        Pageable pageable =
                PageRequest.of(0, 10,
                        Sort.by("csRecipeId").descending()
                                .and(Sort.by("recipeT_title").ascending()));

        Page<Object[]> result = customRecipeRepository.searchPage("tw", "타이틀", pageable);

    }
}
