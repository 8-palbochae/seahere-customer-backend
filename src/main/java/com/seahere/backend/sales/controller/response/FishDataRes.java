package com.seahere.backend.sales.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class FishDataRes {

    private String productName;
    private String productImg;
    private BigDecimal price;

}
