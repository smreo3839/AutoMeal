package org.zerock.ex01.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.ex01.dto.ProductDTO;
import org.zerock.ex01.dto.ResponseDTO;
import org.zerock.ex01.entity.ProductEntity;
import org.zerock.ex01.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("shop")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/itemList")
    public ResponseEntity<?> getProductList(){
        log.info("getProductList() called");
        //서비스 메소드 이용해 상품리스트를 가져옴
        return anything(service.getProductList());
    }

    //상품명 검색 용
    @GetMapping("/productListByPName")
    public ResponseEntity<?> getProductListByPName(@RequestParam String productName) {
        log.info("getProductListByPName called");

        return  anything(service.getProductListByProductName(productName));

    }

    //상품 품번일치 검색
    @GetMapping("/productFindByPId")
    public ResponseEntity<?> getProductFindByPId(@RequestParam Long productId) {
        log.info("getProductByPId called");

        return anything(service.getProductByProductId(productId));

    }

    //쇼핑몰연계를 위한 상품 찾기
    @GetMapping("/productFindByPCate")
    public ResponseEntity<?> getProductFindByPCate(@RequestParam String productCategory) {
        log.info("getProductFindByPCate called");
        return anything(service.getProductByProductCategory(productCategory));
    }

    //쇼핑몰 분류 상품 찾기
    @GetMapping("/productListByPClass")
    public ResponseEntity<?> getProductListByPClass(@RequestParam String productClassification) {
        log.info("getProductListByPClass called");
        return anything(service.getProductListByProductClassification(productClassification));
    }

    @GetMapping("/productListByPrice")
    public ResponseEntity<?> getProductListByPrice(@RequestParam Integer price){
        log.info("getProductListByPrice called");
        return anything(service.getProductListLessThanPrice(price));
    }


    public ResponseEntity<?> anything(List<ProductEntity> getEntities){
        List<ProductEntity> entities = getEntities;
        List<ProductDTO> dtos = entities.stream().map(ProductDTO::new).collect(Collectors.toList());
        ResponseDTO<ProductDTO> response= ResponseDTO.<ProductDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }


}
