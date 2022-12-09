package org.zerock.ex01.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"customRecipe", "replyer"})//callSuper는 상속받은 필드값까지 출력 여부 지정
public class CustomRecipeReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto imcrement
    private Long rp_num;//댓글번호
    private String rp_content;//댓글 내용
    private String imgUrl;//댓글 이미지 경로
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    //지연로딩, 기본값으로는 참조 하는 테이블들의 정보값을 조인으로 가져오기에 불필요한 데이터값은 조인하지 않기위해 fetch 설정
    //지연로딩은 @Transactional을 붙인 메서드에서 get으로 불러오면 수동으로 조인이 가능하다.
    private CustomRecipe customRecipe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User replyer;

    public CustomRecipeReply(CustomRecipeReply eachCustomRecipeReply) {
        super();
    }
}
