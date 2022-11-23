package org.zerock.ex01.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.CustomRecipeDTO;
import org.zerock.ex01.dto.PageRequestDTO;
import org.zerock.ex01.dto.PageResultDTO;
import org.zerock.ex01.entity.CustomRecipe;
import org.zerock.ex01.entity.FoodImage;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.CustomRecipeReplyRepository;
import org.zerock.ex01.repository.CustomRecipeRepository;
import org.zerock.ex01.repository.FoodImageRepository;

import java.io.File;
import java.util.List;
import java.util.function.Function;


@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class CustomRecipeServiceImpl implements CustomRecipeService {

    private final CustomRecipeRepository repository;
    private final CustomRecipeReplyRepository replyRepository;
    private final FoodImageRepository foodImageRepository;

    @Autowired
    AmazonS3Client amazonS3Client;

    @Override
    public Long register(CustomRecipeDTO dto) {
        log.info("DTO");
        CustomRecipe entity = dtoToEntity(dto);//dto를 entity타입으로 변환
        log.info(entity);
        repository.save(entity);
        return entity.getCsRecipeId();
    }

    @Override
    public PageResultDTO<CustomRecipeDTO, Object[]> getList(PageRequestDTO requestDTO) {
        //Pageable pageable = requestDTO.getPageable(Sort.by("csRecipeId").descending());//정렬 기준을 매개변수 값으로 넘기고 pageRequest 객체 생성

//        Page<Object[]> result = repository.getCustomRecipeReplyCount(requestDTO.getPageable(Sort.by("csRecipeId").descending()));//JPA 처리 결과 => (커스텀 레시피 리스트)
//        Function<Object[], CustomRecipeDTO> fn = (entity -> entityToDto((CustomRecipe) entity[0], (User) entity[1], (Long) entity[2]));
//        //엔티티 객체들을 DTO의 리스트로 변환하기위해 함수 저장
        Page<Object[]> result = repository.searchPage(
                requestDTO.getType(),
                requestDTO.getKeyword(),
                requestDTO.getPageable(Sort.by("csRecipeId").descending()));
        // Page<Object[]> result = repository.getCustomRecipeReplyCount(requestDTO.getPageable(Sort.by("csRecipeId").descending()));//JPA 처리 결과 => (커스텀 레시피 리스트)
        Function<Object[], CustomRecipeDTO> fn = (entity -> entityToDto((CustomRecipe) entity[0], (User) entity[1], (Long) entity[2]));
        //엔티티 객체들을 DTO의 리스트로 변환하기위해 함수 저장

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public CustomRecipeDTO get(Long csRecipeId) {

        Object result = repository.getCustomRecipeByCsRecipeId(csRecipeId);

        Object[] arr = (Object[]) result;

        return entityToDto((CustomRecipe) arr[0], (User) arr[1], (Long) arr[2]);
    }


    @Transactional
    @Override
    public void removeWithReplies(Long bno) {//게시물과 해당 게시물의 댓글들 삭제
        //댓글 부터 삭제

        List<FoodImage> list = foodImageRepository.getImgList(bno);
        for (FoodImage foodImage : list) {
            try {
                amazonS3Client.deleteObject(UploadImgService.S3Bucket, foodImage.getPath().replace(File.separatorChar, '/'));//s3의 이미지 파일들 삭제
            } catch (AmazonServiceException e) {
                log.info(e.getErrorMessage());
            }
        }
        replyRepository.deleteByBno(bno);
        repository.deleteById(bno);
    }

    @Override
    public Long modify(CustomRecipeDTO dto) {
        //업데이타 하는 항목은 '제목', '내용', '레시피 카테고리'
        CustomRecipe entity = repository.getOne(dto.getCsRecipeId());

        entity.changeRecipeT_title(dto.getRecipeT_title());//제목 수정
        entity.changeRecipe_content(dto.getRecipe_content());//내용 수정
        entity.changeRecipe_category(dto.getRecipe_category());//레시피 카테고리 수정

        repository.save(entity);

        return entity.getCsRecipeId();
    }

}

