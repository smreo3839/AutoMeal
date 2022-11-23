package org.zerock.ex01.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @Column(length = 50)
    private String userEmail;//유저 이메일
    @Column(length = 50)
    private String phoneNum;//전화번호
    @Column(length = 50)
    private String address;//주소
    @Column(length = 50)
    private String token;//간편 로그인 토큰
    @Column(length = 50)
    private String img;//프로필
    @Column(length = 50)
    private String diet;//유저 식단 정보
    @Column(length = 50)
    private String nickName;//유저 닉네임
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<RecipeBookMark> recipeBookMarks;
}
