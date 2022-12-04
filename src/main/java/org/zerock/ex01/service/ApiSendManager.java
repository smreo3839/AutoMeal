package org.zerock.ex01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ApiSendManager<T> {
    public String dtoConvertStringParam(T t) {

        ObjectMapper objectMapper = new ObjectMapper();
        Map result = objectMapper.convertValue(t, Map.class);
        String strReuslt = "";
        for (Object key : result.keySet()) {
            Object value = result.get(key);
            if (value != null) {
                strReuslt += ("&" + key + "=" + value);
            }
        }
        return strReuslt;
    }

    public Map<String, Object> sendGetRequestToApi(String apiKeys, String url) {
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

    public Map<String, Object> sendPostRequestToApi(String apiKeys, String url, Map<String, String> map) {
        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        // Header 및 Body 설정
        HttpHeaders headers = new HttpHeaders();
        // 2. 헤더 설정 : ContentType, Accept 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("x-api-key", apiKeys);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        for (String key : map.keySet()) {
            body.add(key, map.get(key));
        }

        // 설정한 Header와 Body를 가진 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<?> resultMap = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        //exchange() 메소드로 api를 호출 후 요청한 결과를 HashMap에 추가

        // convert object to map => ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Json 문자열로 변환
        Map<String, Object> objMap = objectMapper.convertValue(resultMap.getBody(), Map.class);//resultMap.getBody() => ResponseEntity의 데이터 정보 확인
        return objMap;
    }

    public Map<String, Object> sendPostRequestImgToApi(String target, String apiKeys, String url, Map<String, Object> map) throws IOException {
        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        // Header 및 Body 설정
        HttpHeaders headers = new HttpHeaders();
        // 2. 헤더 설정 : ContentType, Accept 설정

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add(target, apiKeys);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (String key : map.keySet()) {
            ByteArrayResource resource = new ByteArrayResource(((MultipartFile) map.get(key)).getBytes()) {
                @Override
                public String getFilename() throws IllegalStateException {
                    return ((MultipartFile) map.get(key)).getOriginalFilename();
                }
            };
            body.add(key, resource);
//            body.add(key, map.get(key));
        }

        // 설정한 Header와 Body를 가진 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<?> resultMap = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        //exchange() 메소드로 api를 호출 후 요청한 결과를 HashMap에 추가

        // convert object to map => ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Json 문자열로 변환
        Map<String, Object> objMap = objectMapper.convertValue(resultMap.getBody(), Map.class);//resultMap.getBody() => ResponseEntity의 데이터 정보 확인
        return objMap;
    }
}