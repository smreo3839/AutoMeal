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
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.dto.UploadResultDTO;
import org.zerock.ex01.service.ApiFoodRecipeServiceImpl;
import org.zerock.ex01.service.CustomRecipeService;
import org.zerock.ex01.service.UploadImgService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/CustomRecipe")
@Log4j2
@RequiredArgsConstructor//의존성 자동주입, final 혹은 @NotNull 필드만 의존성 자동 주입이 가능하다
public class CustomRecipeController {

    private final CustomRecipeService customRecipeService;
    private final UploadImgService uploadImgService;


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Long> register(@AuthenticationPrincipal String userId, CustomRecipeDTO dto) throws IOException {//게시글 작성
        log.info(dto);
        dto.setUser_email(userId);
        log.info("register userId {}", userId);
        Long csRecipeId = customRecipeService.register(dto);
        if (dto.getUploadFiles() != null) {
            uploadImgService.uploadFileToAwsS3(dto.getUploadFiles(), csRecipeId);
        }
        return new ResponseEntity<>(csRecipeId, HttpStatus.OK);
    }

    @GetMapping("/nser/list")
    @Transactional
    public ResponseEntity<PageResultDTO> list(PageRequestDTO pageRequestDTO) {//게시글 리스트
        log.info("list");
        PageResultDTO pageResultDTO = customRecipeService.getList(pageRequestDTO);
        uploadImgService.getImageList(pageResultDTO);
        return new ResponseEntity<>(pageResultDTO, HttpStatus.OK);
    }

    @GetMapping({"/nser/get", "/nser/modify"})
    public ResponseEntity<CustomRecipeDTO> read(@ModelAttribute("csRecipeId") Long csRecipeId, PageRequestDTO pageRequestDTO) {//게시글 상세보기, 게시글 수정 요청시 기존 값 전달
        //상세보기에서 다시 목록페이지로 돌아가기 위해 PageRequestDTO 파라미터로 지정
        log.info("read");
        CustomRecipeDTO customRecipeDTO = customRecipeService.get(csRecipeId);
        uploadImgService.getImageList(customRecipeDTO);
        return customRecipeDTO != null ? new ResponseEntity<>(customRecipeDTO, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@AuthenticationPrincipal String userId, @ModelAttribute("csRecipeId") Long csRecipeId) {
        log.info("remove");
        CustomRecipeDTO customRecipeDTO = customRecipeService.get(csRecipeId);
        if (!customRecipeDTO.getUser_email().equals(userId))
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        customRecipeService.removeWithReplies(csRecipeId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/modify")
    @Transactional
    public ResponseEntity<?> modify(@AuthenticationPrincipal String userId, CustomRecipeDTO dto, @RequestParam("defaultUploadImgResult") String defaultUploadImgResultJson) throws IOException {//게시판 수정 연산
        log.info("modify {}", dto);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        List<UploadResultDTO> tempJsonList = objectMapper.readValue(defaultUploadImgResultJson, new TypeReference<List<UploadResultDTO>>() {
        });
        dto.setUploadImgResult(tempJsonList);

        log.info("jsonRe{} ", tempJsonList);
        CustomRecipeDTO customRecipeDTO = customRecipeService.get(dto.getCsRecipeId());
        if (!customRecipeDTO.getUser_email().equals(userId))
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        Long csRecipeId = customRecipeService.modify(dto);
        uploadImgService.modifyToAwsS3(dto.getUploadFiles(), csRecipeId, tempJsonList);
        return csRecipeId != null ? new ResponseEntity<>(csRecipeId, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
