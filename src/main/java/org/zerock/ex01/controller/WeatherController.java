package org.zerock.ex01.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.ex01.dto.GeoCodeDTO;
import org.zerock.ex01.dto.WeatherAdDTO;
import org.zerock.ex01.entity.WeatherAD;
import org.zerock.ex01.service.ApiWeatherService;
import org.zerock.ex01.service.WeatherFoodService;


@Slf4j
@RestController
@RequestMapping("/ad")
public class WeatherController {

    @Autowired
    ApiWeatherService apiWeatherService;

    @Autowired
    WeatherFoodService weatherFoodService;

    @PostMapping
    public ResponseEntity<?> getAd(GeoCodeDTO geoCodeDTO){
        log.info("getAD() called");
        String weather=apiWeatherService.searchWeather(geoCodeDTO);
        log.info("weather: {}",weather);
        WeatherAdDTO weatherAD=weatherFoodService.getWeatherFood(weather.toLowerCase());

        return ResponseEntity.ok().body(weatherAD);
    }

}
