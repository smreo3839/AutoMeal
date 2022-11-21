package org.zerock.ex01.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomRecipeDTO {
    // DTO와 ENTITY는 매우 유사하지만 분리해서 처리하는것을 권장
    private Long csRecipeId;//게시판 번호
    private String user_email;//글쓴이 이메일
    private String nick_name;//글쓴이 닉네임
    private String recipeT_title;//레시피 이름
    private String recipe_content;//레시피 내용
    private int like_rate;//좋아요 수
    private String recipe_category;//레시피 타입(다이어트, 비건)
    private int cr_hits;//조회 수
    private int replyCount;//댓글 수
    private LocalDateTime regDate;//게시글 등록시간
    private LocalDateTime modDate;//게시글 수정시간
    private String thumbnail_url;//게시글 썸네일
    private MultipartFile[] uploadFiles;//이미지 파일
    private List<UploadResultDTO> uploadImgResult;
}
