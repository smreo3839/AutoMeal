package org.zerock.ex01.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String userEmail;//유저 이메일
    @Column(length = 100)
    private String phoneNum;//전화번호

    private String address;//주소

    private String token;//간편 로그인 토큰

    private String img;//프로필
    @Column(length = 50)
    private String diet;//유저 식단 정보
    @Column(length = 100)
    private String nickName;//유저 닉네임

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<RecipeBookMark> recipeBookMarks;

    public void changeDiet(String diet){
        this.diet =diet;
    }

    public void updateUser(User user){
        if(user.getPhoneNum() !=null) {
        this.phoneNum=user.getPhoneNum();
        }
        if(user.getAddress() !=null) {
        this.address = user.getAddress();
        }
        if(user.getDiet()!=null){
            this.diet = user.getDiet();
        }
        if(user.getImg() !=null){
            this.img= user.getImg();
        }
        if(user.getNickName() !=null){
            this.nickName = user.getNickName();
        }
    }

}
