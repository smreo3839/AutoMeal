package org.zerock.ex01.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_payed")
@Getter
@Setter
public class OrderPayed {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

}
