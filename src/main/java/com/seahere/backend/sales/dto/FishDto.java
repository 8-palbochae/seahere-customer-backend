package com.seahere.backend.sales.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@Builder
@NoArgsConstructor
public class FishDto {

    private String productName;
    private String productImg;
    private BigDecimal price;

    public FishDto(String productName, String productImg, BigDecimal price) {
        this.productName = productName;
        this.productImg = productImg;
        this.price = price;
    }
}
