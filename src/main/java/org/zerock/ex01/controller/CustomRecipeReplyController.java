package org.zerock.ex01.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex01.dto.*;
import org.zerock.ex01.service.CustomRecipeService;
import org.zerock.ex01.service.CustomRecipeServiceReply;
import org.zerock.ex01.service.UploadImgService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/CustomRecipeReply")
@Log4j2
@RequiredArgsConstructor//의존성 자동주입, final 혹은 @NotNull 필드만 의존성 자동 주입이 가능하다
public class CustomRecipeReplyController {
    private final CustomRecipeServiceReply customRecipeServiceReply;
    private final UploadImgService uploadImgService;

    @GetMapping("/nser/list")
    @Transactional
    public ResponseEntity<?> list(PageRequestDTO pageRequestDTO) {//게시글 리스트
        log.info("list");
        return new ResponseEntity<>(customRecipeServiceReply.getList(pageRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Long> register(@AuthenticationPrincipal String userId, CustomRecipeReplyDTO dto) throws IOException {//게시글 작성
        log.info(dto);
        log.info("register userId {}", userId);
        Long rp_num = customRecipeServiceReply.register(userId, dto);
        return new ResponseEntity<>(rp_num, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@AuthenticationPrincipal String userId, @RequestBody CustomRecipeReplyDTO dto) {
        log.info("remove");
        CustomRecipeReplyDTO customRecipeDTO = customRecipeServiceReply.get(dto);
        if (!customRecipeDTO.getWriter().getUserEmail().equals(userId))
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        customRecipeServiceReply.removeWithReplies(dto);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}