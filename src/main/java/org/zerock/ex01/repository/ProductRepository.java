package org.zerock.ex01.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex01.entity.ProductEntity;

import java.util.List;


public interface ProductRepository extends JpaRepository<ProductEntity,Long > {

    //상품아이디로 상품 찾기
    //List<ProductEntity> findByProductId(Long productId);

    ProductEntity findByProductId(Long productId);

    //상품명으로 상품 찾기
    @Query("SELECT p FROM ProductEntity p where p.productName like %:productName% order by p.price desc")
    List<ProductEntity> findByProductName(@Param("productName") String productName);

    //쇼핑몰연계를 위한 세부 상품목록 찾기
   List<ProductEntity> findByProductCategory(String productCategory);


   //상품분류로 상품찾기
    List<ProductEntity> findByProductClassification(String productClassification);

    //상품 검색금액 얼마 이하만 찾기
    List<ProductEntity> findByPriceLessThanEqualOrderByPriceDesc(Integer price);

}
