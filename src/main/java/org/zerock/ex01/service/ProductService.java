package org.zerock.ex01.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.ex01.entity.ProductEntity;
import org.zerock.ex01.repository.ProductRepository;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<ProductEntity> getProductList(){
        log.info("상품목록 보내주기");
        return repository.findAll();
    }

    public List<ProductEntity> getProductListByProductName(final String productName){
        log.info("상품목록 이름으로 찾기");
        return repository.findByProductName(productName);
    }

    //상품 품번으로 상품 찾기
//    public List<ProductEntity> getProductByProductId(final Long productId){
//        log.info("상품 품번으로 찾기");
//        return repository.findByProductId(productId);
//    }
    public ProductEntity getProductByProductId(final Long productId){
        log.info("상품 품번으로 찾기");
        return repository.findByProductId(productId);
    }


    //쇼핑몰연계를위한 상품 목록 찾기
    public List<ProductEntity> getProductByProductCategory(final String productCategory){
        log.info("쇼핑몰연계를위한 상품 목록 찾기");
        return repository.findByProductCategory(productCategory);
    }

    //상품분류로 리스트 가져오가
    public List<ProductEntity> getProductListByProductClassification(final String productClassification){
        log.info("상품분류로 리스트 가져오기");
        return repository.findByProductClassification(productClassification);
    }

    //상품 얼마 이하 리스트가져오기
    public List<ProductEntity> getProductListLessThanPrice(final int price){
        log.info("상품 얼마 이하 리스트가져오기");
        return repository.findByPriceLessThanEqualOrderByPriceDesc(price);
    }



}
