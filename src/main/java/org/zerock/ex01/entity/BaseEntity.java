package org.zerock.ex01.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@ToString
abstract class BaseEntity {

    @CreatedDate//데이터 생성 시간
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate//데이터 마지막 수정 시간
    @Column(name ="moddate")
    private LocalDateTime modDate;

}
