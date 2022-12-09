package org.zerock.ex01.service;

import org.zerock.ex01.dto.GeoCodeDTO;

import java.util.Map;


public interface ApiWeatherService {

    String searchWeather(GeoCodeDTO geoCodeDTO);
}
