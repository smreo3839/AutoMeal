package org.zerock.ex01.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherAD {

    @Id
    private String weatherType;

    private String icon;

    private String imgUrl;

    private Long id;

    private String title;


}
