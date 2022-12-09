package org.zerock.ex01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.ex01.dto.GeoCodeDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class ApiWeatherServiceImpl implements ApiWeatherService{

    private final ApiSendManager apiSendManager;

    @Value("${apikeyW}")
    private String apiKeyW;

    @Override
    public String searchWeather(GeoCodeDTO geoCodeDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("searchWeather 날씨 정보 얻어오기");
        Map<String, Object> map = apiSendManager.sendGetRequestToApi(null, "https://api.openweathermap.org/data/2.5/weather?lat="+geoCodeDTO.getLat()+"&lon="+geoCodeDTO.getLon()+"&appid="+apiKeyW);
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) map.get("weather");
        String weather=weatherList.get(0).get("main").toString();
        return weather;
    }
}
