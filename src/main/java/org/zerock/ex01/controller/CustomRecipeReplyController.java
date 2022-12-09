package org.zerock.ex01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.CustomRecipeReplyDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.service.CustomRecipeService;
import org.zerock.ex01.service.CustomRecipeServiceReply;
import org.zerock.ex01.service.UploadImgService;

import java.io.IOException;

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
        Long rp_num = customRecipeServiceReply.register(userId,dto);
        if (dto.getUploadFile() != null) {
            uploadImgService.uploadFileToAwsS3(dto.getUploadFile(), rp_num);
        }
        return new ResponseEntity<>(rp_num, HttpStatus.OK);
    }
}
