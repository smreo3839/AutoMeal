package org.zerock.ex01.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.ex01.entity.ProductEntity;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private Long product_id;
    private int price;
    private String product_classification;
    private String product_name;
    private String description;
    private String image_first;
    private String image_second;
    private String image_third;
    private String product_category;

    public ProductDTO(ProductEntity entity) {
        this.product_id = entity.getProductId();
        this.price = entity.getPrice();
        this.product_classification = entity.getProductClassification();
        this.product_name = entity.getProductName();
        this.product_category=entity.getProductCategory();
        this.description = entity.getDescription();
        this.image_first = entity.getImageFirst();
        this.image_second = entity.getImageSecond();
        this.image_third = entity.getImageThird();
    }

    public static ProductEntity toEntity(final ProductDTO dto){
        return ProductEntity.builder()
               .productId(dto.getProduct_id())
               .price(dto.getPrice())
                .productCategory(dto.getProduct_category())
                .productClassification(dto.getProduct_classification())
                .productName(dto.getProduct_name())
               .description(dto.getDescription())
               .imageFirst(dto.getImage_first())
                .imageSecond(dto.getImage_second())
                .imageThird(dto.getImage_third())
                .build();
    }

}
