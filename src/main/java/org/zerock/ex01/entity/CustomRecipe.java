package org.zerock.ex01.entity;

import java.util.Date;
import java.util.List;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "writer")
public class CustomRecipe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto imcrement
    private Long csRecipeId;//게시판 번호
    @Column(length = 50)
    private String recipeT_title;//레시피 이름
    @Column(length = 200)
    private String recipe_content;//레시피 내용
    private int like_rate;//좋아요 수
    @Column(length = 20)
    private String recipe_category;//레시피 타입(다이어트, 비건)
    private int cr_hits;//조회수
    @ManyToOne//레시피와 유저 간의 1:N 관계
    private User writer;//글쓴이 이메일
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "custom_recipe", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<FoodImage> foodImages;


    public void changeRecipeT_title(String recipeT_title) {
        this.recipeT_title = recipeT_title;
    }

    public void changeRecipe_content(String recipe_content) {
        this.recipe_content = recipe_content;
    }

    public void changeRecipe_category(String recipe_category) {
        this.recipe_category = recipe_category;
    }
}
