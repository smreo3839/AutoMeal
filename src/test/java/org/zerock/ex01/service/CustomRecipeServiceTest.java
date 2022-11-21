package org.zerock.ex01.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.dto.PageResultDTO;

@SpringBootTest
public class CustomRecipeServiceTest {
    @Autowired
    CustomRecipeService service;

    @Test
    public void testList() {

        //1페이지 10개
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<CustomRecipeDTO, Object[]> result = service.getList(pageRequestDTO);

        for (CustomRecipeDTO dto : result.getDtoList()) {
            System.out.println(dto);
        }

    }

}
