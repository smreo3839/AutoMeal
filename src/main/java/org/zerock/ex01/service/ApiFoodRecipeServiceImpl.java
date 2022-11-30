package org.zerock.ex01.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import marvin.image.MarvinImage;
import marvinplugins.MarvinPluginCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.ex01.dto.MainRecipeDTO;
import org.zerock.ex01.dto.UserDTO;
import org.zerock.ex01.repository.RecipeBookMarkRepository;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class ApiFoodRecipeServiceImpl implements ApiFoodRecipeService {
    private final RecipeBookMarkRepository recipeBookMarkRepository;
    private final ApiSendManager apiSendManager;
    @Value("${apikeystoDB}")
    private String apiKeysToDB;
    @Value("${apiKeysToDetection}")
    private String apiKeysToDetection;


    @Override
    public Map<String, Object> getRandomRecipe() {
        log.info("getRandomRecipe");
        Map<String, Object> map = apiSendManager.sendGetRequestToApi(apiKeysToDB, "https://api.spoonacular.com/recipes/random?number=20");
        List<Object> list = (List<Object>) map.get("recipes");
        return map;
    }

    @Override
    public Map<String, Object> getDetailRecipe(String recipeId) {
        log.info("getDetailRecipe");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Recipe_Information", apiSendManager.sendGetRequestToApi(apiKeysToDB, "https://api.spoonacular.com/recipes/" + recipeId + "/information?includeNutrition=true"));
        result.put("Recipe_Taste", apiSendManager.sendGetRequestToApi(apiKeysToDB, "https://api.spoonacular.com/recipes/" + recipeId + "/tasteWidget.json"));
        return result;
    }

    @Override
    public Map<String, Object> searchRecipes(UserDTO userDTO, MainRecipeDTO recipeDTO) {
        log.info("searchRecipes");
        Map<String, Object> map = new HashMap<>();
        // ObjectMapper
        Map<String, Object> temp = apiSendManager.sendGetRequestToApi(apiKeysToDB, "https://api.spoonacular.com/recipes/complexSearch?sort=popularity&addRecipeInformation=true" +
                apiSendManager.dtoConvertStringParam(recipeDTO));
        log.info(temp);
        List<Object> results = new ArrayList<>();
        for (Object obj : ((List<Object>) temp.get("results"))) {
            Map<String, Object> objToMap = (Map<String, Object>) obj;
            results.add(objToMap.entrySet().stream().filter(f -> f.getKey().equals("id") || f.getKey().equals("title") || f.getKey().equals("image"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        log.info(map);
        //map = checkBookMark(dto, map);
        if (userDTO != null) {
            map.put("results", results);
            map.put("BookMarkList", recipeBookMarkRepository.findAllBookMark(userDTO.getUserEmail()));
            map.put("makePageList", recipeDTO.makePageList((Integer) temp.get("totalResults")));
        }
        return map;
    }


    public Map<String, Object> returnResultMap(Map.Entry<String, Object> entry) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(entry.getKey(), entry.getValue());
        return map;

    }

    @Override
    public List<?> ingredientDetection(MultipartFile[] uploadFiles) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<?> resultList = null;
        List finalResult = new ArrayList<>();
        for (MultipartFile uploadFile : uploadFiles) {
            try {
                resultList = (List) apiSendManager.sendPostRequestImgToApi(apiKeysToDetection, "https://api.logmeal.es/v2/image/segmentation/complete/v1.0", Collections.singletonMap("image", uploadFile)).get("segmentation_results");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info(resultList);

            //Map<String, Object> finalResult = new HashMap<>();
            // List finalResult = new ArrayList<>();
            Map<String, Object> temp = null;
            for (Object obj : resultList) {
                temp = objectMapper.convertValue(obj, Map.class);
                List detectionList = new ArrayList<>();

                for (Object objSecond : (List) temp.get("recognition_results")) {
                    Map<String, Object> tempSecond = objectMapper.convertValue(objSecond, Map.class);
                    detectionList.add(Collections.singletonMap(tempSecond.get("name"), tempSecond.get("prob")));
                }
                temp.clear();
                temp.put("detectionList", detectionList);//식재료 식별 리스트
                temp.put("base64Img", imageCrop(uploadFile, objectMapper.convertValue(objectMapper.convertValue(obj, Map.class).get("contained_bbox"), Map.class)));//이미지 base64 문자열
                finalResult.add(temp);
            }
        }
        return finalResult;
    }

    public String imageCrop(MultipartFile uploadFile, Map<String, Integer> contained_bbox) {//원본이미지를 x,y,width,height 좌표로 리사이징
        MarvinImage imageIn = null;
        try {
            imageIn = new MarvinImage(ImageIO.read(uploadFile.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MarvinImage imageOut = new MarvinImage();
        // crop the position of eyes
        MarvinPluginCollection.crop(imageIn, imageOut, contained_bbox.get("x"), contained_bbox.get("y"), contained_bbox.get("w"), contained_bbox.get("h"));
        return encodeToString(imageOut.getBufferedImageNoAlpha(), imageOut.getFormatName());
    }

    public String encodeToString(BufferedImage image, String type) {//base64로 이미지 인코딩 후 문자열로 리턴

        log.info(image);
        String base64String = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            base64String = DatatypeConverter.printBase64Binary(bos.toByteArray());
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64String;
    }

//    public Map<String, Object> checkBookMark(UserDTO dto, Map<String, Object> map) {//api 레시피 리스트들과 db 북마크 리스트 비교
//        List<String> checkList = recipeBookMarkRepository.findAllBookMark(dto.getUserEmail());
//        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("results");
//        List Resultlist = list.stream().map(obj -> check(obj, checkList.contains(obj.get("id").toString()))).collect(Collectors.toList());
//        log.info(Resultlist);
//        return null;
//    }
//
//    public Map<String, Object> check(Map<String, Object> map, boolean flag) {
//        map.put("checkBookMark", flag);
//        return map;
//    }

}
