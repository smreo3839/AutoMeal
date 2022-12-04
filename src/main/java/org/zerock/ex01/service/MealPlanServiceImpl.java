package org.zerock.ex01.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.*;
import org.zerock.ex01.entity.MealPlan;
import org.zerock.ex01.entity.MealPlanList;
import org.zerock.ex01.repository.MealPlanListRepostiory;
import org.zerock.ex01.repository.MealPlanRepostiory;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class MealPlanServiceImpl implements MealPlanService {
    private final ApiSendManager<GenerateMealPlanDTO> apiSendManager;
    private final MealPlanRepostiory mealPlanRepostiory;
    private final MealPlanListRepostiory mealPlanListRepostiory;
    @Value("${apikeystoDB}")
    private String apiKeysToDB;


    @Override
    public List<Object> generateMealPlan(GenerateMealPlanDTO generateMealPlanDTO) {//하루 또는 주 레시피 생생 후 저장
        Map<String, Object> resultMap = apiSendManager
                .sendGetRequestToApi(apiKeysToDB, "https://api.spoonacular.com/mealplanner/generate?" + apiSendManager.dtoConvertStringParam(generateMealPlanDTO));
        List<Object> resultList = new ArrayList();
        if (resultMap.containsKey("week")) {
            Map<String, Map<String, Object>> planRs = (Map<String, Map<String, Object>>) resultMap.get("week");
            for (String key : planRs.keySet()) {//monday,tuesday...
                resultList.add(planRs.get(key).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                //List<?> tempList = tempList = meals.stream().map(s -> s.entrySet().stream().filter(e -> "id".equals(e))).collect(Collectors.toList());
                //   List<?> tempList = tempList = meals.stream().map(s -> String.valueOf(s.get("id"))).collect(Collectors.toList());
            }
        } else {
            resultList.add(resultMap);
        }
        return resultList;
    }

    //    @Override
//    @Transactional
//    public void addMealPlan(String userEmail, List<MealPlanRequestDTO> mealPlanResultList) {//람다 미사용 버전
//        List<MealPlanList> tempList = new ArrayList();
//        for (MealPlanRequestDTO mealPlanRequestDTO : mealPlanResultList) {
//            MealPlan mealPlan = dtoToMealPlanEntity(mealPlanRequestDTO, userEmail);
//            Long mealPlanid = mealPlanRepostiory.save(mealPlan).getPlanId();//PK
//            for (MealPalnDTO mealPalnDTO : mealPlanRequestDTO.getMeals()) {
//                tempList.add(dtoToMealPlanListEntity(mealPalnDTO, mealPlanid));
//            }
//        }
    @Override
    @Transactional
    public void addMealPlan(String userEmail, List<MealPlanResulttDTO> mealPlanResultList) {
        List<MealPlanList> tempList = new ArrayList();
        for (MealPlanResulttDTO mealPlanResulttDTO : mealPlanResultList) {
            MealPlan mealPlan = dtoToMealPlanEntity(mealPlanResulttDTO, userEmail);
            Long mealPlanid = mealPlanRepostiory.save(mealPlan).getPlanId();//PK
            tempList.addAll(mealPlanResulttDTO.getMeals().stream().map(dto -> dtoToMealPlanListEntity(dto, mealPlanid)).collect(Collectors.toList()));
        }
        mealPlanListRepostiory.saveAll(tempList);
    }

    @Override
    public List<MealPlanResulttDTO> getMealPlan(String userEmail) {
        //해당유저의 이메일로 MealPlan 검색후 entity객체를 dto객체로 변환 후 리턴
        return mealPlanRepostiory.findByUserEmail(userEmail).stream().map(entity -> entityToMealPlanResulttDto(entity)).collect(Collectors.toList());
    }

    @Override
    public void ChangeStatus(Long planListId) {
        mealPlanListRepostiory.changeStatus(planListId);
    }

    @Override
    public void StatusAllReset(String userEmail) {

        List<MealPlanList> mealPlanListList = mealPlanRepostiory.findByUserEmail(userEmail)
                .stream()
                .flatMap(etyList -> etyList.getMealPlanListList().stream())
                .filter(MealPlanList::isClearState)
                .map(s -> {
                    s.resetClearState();
                    return s;//map은 값을 변환후 반환해야만 하기에 resetClearState로 값 변경후 return s로 값 반환
                }).collect(Collectors.toList());//해당 유저의 mealPlan 리스트를 가져온 후 mealPlanList entity의 필드값인 clearstatus 모두 false로 변경후 update

        mealPlanListRepostiory.saveAll(mealPlanListList);
//        mealPlanRepostiory.findByUserEmail(userEmail).stream().flatMap(etyList -> etyList.getMealPlanListList().stream()).peek(s -> {
//            s.resetClearState();
//        }).collect(Collectors.toList());

    }

    @Override
    public Long checkMealPlanCount(String userEmail) {
        return mealPlanRepostiory.countForUserEmail(userEmail);//유저의 mealPlan 개수 검색
    }

    @Override
    public void removeMealPlan(Long planId) {
        mealPlanRepostiory.deleteById(planId);
    }


}