package org.zerock.ex01.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.CustomRecipeReply;
import org.zerock.ex01.entity.User;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private CustomRecipeReplyRepository customRecipeReplyRepository;

//    @Test
//    public void insertReply() {
//
//        IntStream.rangeClosed(1, 300).forEach(i -> {
//            //1부터 100까지의 임의의 번호
//            long bno = (long) (Math.random() * 10) + 1;
//            User user = User.builder().user_email("test10@gmail.com").build();
//            CustomRecipe customRecipe = CustomRecipe.builder().csRecipeId(bno).writer(user).build();
//
//            CustomRecipeReply reply = CustomRecipeReply.builder()
//                    .rp_content("Reply......." + i)
//                    .custom_recipe(customRecipe)
//                    .replyer(user)
//                    .build();
//
//            customRecipeReplyRepository.save(reply);
//
//        });
//
//    }

    @Test
    public void readReply1() {

        Optional<CustomRecipeReply> result = customRecipeReplyRepository.findById(3L);

        CustomRecipeReply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getReplyer());

    }


}
