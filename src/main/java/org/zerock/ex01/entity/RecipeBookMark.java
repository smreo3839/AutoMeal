package org.zerock.ex01.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = "user") //연관 관계시 항상 주의
@DynamicUpdate
public class RecipeBookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bmNum;//장바구니 번호
    private String recipe_id;//레시피 번호
    private boolean book_mark;//북마크 여부
    private boolean recipeDone;//해당 레시피 조리 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
        /*
        연관관계의 주인은 mappedby 를 사용하지 않는다.
        연관관계의 주인은 외래키가 있는 쪽이 주인이 된다.
        JoinColumn(name) => RecipeBookMark에서 어던 이름의 컬럼명을 할 것인지 설정
        JoinColumn(referencedColumnName) =>외래 키 열이 참조하는 열의 이름, referencedColumnName 속성을 생략하면 자동으로 대상 테이블의 pk 값으로 지정
    */
    private User user;

}
