package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.WeatherAD;


public interface WeatherFoodRepository  extends JpaRepository<WeatherAD, String> {

    WeatherAD getWeatherADByWeatherType(String weatherType);

}
