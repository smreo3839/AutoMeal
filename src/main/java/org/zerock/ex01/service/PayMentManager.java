package org.zerock.ex01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class PayMentManager {
    @Value("${impoart_imp_key}")
    private String imp_key;
    @Value("${impoart_imp_secret}")
    private String imp_secret;

    public boolean canclePay(String token, String imp_uid) {
        RestTemplate restTemplate = new RestTemplate();

        // Header 및 Body 설정
        HttpHeaders headers = new HttpHeaders();
        // 2. 헤더 설정 : ContentType, Accept 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", token);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("imp_uid", imp_uid);
        // 설정한 Header와 Body를 가진 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<?> resultMap = restTemplate.postForEntity("https://api.iamport.kr/payments/cancel", entity, Object.class);
        // convert object to map => ObjectMapper
        String resCode = resultMap.getStatusCode().toString();
        return resCode.equals("200");
    }


    public String getToken02() {//spring 라이브러리 사용 version
        // 타임아웃 설정
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 타임아웃 설정 5초
        factory.setReadTimeout(5000); // 타임아웃 설정 5초

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate(factory);

        // Header 및 Body 설정
        HttpHeaders headers = new HttpHeaders();
        // 2. 헤더 설정 : ContentType, Accept 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("imp_key", imp_key);
        body.add("imp_secret", imp_secret);
        // 설정한 Header와 Body를 가진 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<?> resultMap = restTemplate.postForEntity("https://api.iamport.kr/users/getToken", entity, Object.class);
        //exchange() 메소드로 api를 호출 후 요청한 결과를 HashMap에 추가

        // convert object to map => ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Json 문자열로 변환
        Map<String, Object> objMap = objectMapper.convertValue(resultMap.getBody(), Map.class);//resultMap.getBody() => ResponseEntity의 데이터 정보 확인
        Gson gson = new Gson();

        // Json 문자열 -> Map 객체로 변환 후 access_token 키 값의 value값을 추출
        String access_token = gson.fromJson(String.valueOf(objMap.get("response")), Map.class).get("access_token").toString();
        return access_token;
    }


    public String getToken() throws IOException {
        HttpsURLConnection conn = null;

        URL url = new URL("https://api.iamport.kr/users/getToken");

        // URL 연결 (웹페이지 URL 연결.)
        conn = (HttpsURLConnection) url.openConnection();

        // 요청 방식 선택 (GET, POST)
        conn.setRequestMethod("POST");
        // 타입설정(text/html) 형식으로 전송 (Request Body 전달시 application/xml로 서버에 전달.)
        conn.setRequestProperty("Content-type", "application/json");
        // 서버 Response Data를 json 형식의 타입으로 요청.
        conn.setRequestProperty("Accept", "application/json");
        // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
        conn.setDoOutput(true);

        JsonObject json = new JsonObject();

        json.addProperty("imp_key", imp_key);
        json.addProperty("imp_secret",
                imp_secret);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        // 출력
        bw.write(json.toString());
        // 버퍼 비움
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        Gson gson = new Gson();
        // Json 문자열 -> Map 객체로 변환 후 response 키 값의 value값을 추출
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

        System.out.println(response);
        // Json 문자열 -> Map 객체로 변환 후 access_token 키 값의 value값을 추출
        String token = gson.fromJson(response, Map.class).get("access_token").toString();

        br.close();
        conn.disconnect();

        return token;
    }


    public String CertificationCheck(String token, String imp_uid) throws IOException, ParseException {
        HttpsURLConnection conn = null;
        URL url = new URL("https://api.iamport.kr/certifications/" + imp_uid);
        // URL 연결 (웹페이지 URL 연결.)
        conn = (HttpsURLConnection) url.openConnection();

        // 요청 방식 선택 (GET, POST)
        conn.setRequestMethod("GET");
        // 타입설정(text/html) 형식으로 전송 (Request Body 전달시 application/xml로 서버에 전달.)
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Authorization", token);
        // 서버 Response Data를 json 형식의 타입으로 요청.
//        conn.setRequestProperty("Accept", "application/json");
        // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
        conn.setDoOutput(true);

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            sb.append(line);
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(sb.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject PetInfoResult = (JSONObject) jsonObject.get("response");
        conn.disconnect();
        return String.valueOf(PetInfoResult.get("phone"));
    }

//    public static void main(String[] args) {
//        PayMentManager manager = new PayMentManager();
//        manager.canclePay(manager.getToken02(), "imp_150143014047");
//    }


}

