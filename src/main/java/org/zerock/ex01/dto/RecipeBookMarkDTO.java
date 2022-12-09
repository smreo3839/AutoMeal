package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeBookMarkDTO {
    private Long bmNum;//북마크 번호
    private String recipe_id;//레시피 번호
    private boolean book_mark;//북마크 여부
    private boolean recipe_done;//해당 레시피 조리 여부
    private String user_email;//북마크 등록할 유저 이메일
}
