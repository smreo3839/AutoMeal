package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherAdDTO {

    private String weatherType;

    private String icon;

    private String imgUrl;

    private Long id;

    private String title;
}
