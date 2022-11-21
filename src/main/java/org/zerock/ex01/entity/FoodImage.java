package org.zerock.ex01.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "custom_recipe") //연관 관계시 항상 주의
public class FoodImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;
    private String real_path;

    @ManyToOne(fetch = FetchType.LAZY) //무조건 lazy로, 지연 조인 설정
    @JoinColumn(name = "cs_recipe_id", referencedColumnName = "csRecipeId")
    private CustomRecipe custom_recipe;


}
