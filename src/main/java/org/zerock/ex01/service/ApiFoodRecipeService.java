package org.zerock.ex01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.zerock.ex01.dto.UserDTO;

import java.util.Map;

public interface ApiFoodRecipeService {
    static final String apiKeys = "a25af8e9ecc7414790c0eae1524264a2";
    default Map<String, Object> sendGetRequestToApi(String url) {
        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        // Header 및 Body 설정
        HttpHeaders headers = new HttpHeaders();
        // 2. 헤더 설정 : ContentType, Accept 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("x-api-key", apiKeys);
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("number", "20");
        // 설정한 Header와 Body를 가진 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        ResponseEntity<?> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        //exchange() 메소드로 api를 호출 후 요청한 결과를 HashMap에 추가

        // convert object to map => ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Json 문자열로 변환
        Map<String, Object> objMap = objectMapper.convertValue(resultMap.getBody(), Map.class);//resultMap.getBody() => ResponseEntity의 데이터 정보 확인
        return objMap;
    }
    Map<String, Object> getRandomRecipe();

    Map<String, Object> getDetailRecipe(String recipeId);


    Map<String, Object> searchRecipes(UserDTO dto, String query);
}
