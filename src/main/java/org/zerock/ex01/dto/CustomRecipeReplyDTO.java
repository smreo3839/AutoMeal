package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomRecipeReplyDTO {
    private Long rp_num;//댓글번호
    private String rp_content;//댓글 내용
    private String imgUrl;//댓글 이미지 경로
    private String writerEmail;//글쓴이 이메일
    private String writerNickName;//글쓴이 닉네임
    private Long csRecipeId;//게시판 번호
    private MultipartFile[] uploadFile;
    private UserDTO writer;
}
