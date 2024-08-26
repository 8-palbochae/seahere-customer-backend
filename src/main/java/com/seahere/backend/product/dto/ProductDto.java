package com.seahere.backend.product.dto;

import com.seahere.backend.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
public class ProductDto {

    private Long productId;
    private String productName;
    private String qr;
    private String productImg;

    public static ProductDto from(ProductEntity productEntity){
        return ProductDto.builder()
                .productId(productEntity.getProductId())
                .productName(productEntity.getProductName())
                .qr(productEntity.getQr())
                .productImg(productEntity.getProductImg())
                .build();
    }
}