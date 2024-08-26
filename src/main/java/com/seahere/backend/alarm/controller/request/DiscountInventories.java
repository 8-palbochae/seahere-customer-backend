package com.seahere.backend.alarm.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@ToString
public class DiscountInventories {
    private Long inventoryId;
    private String name;
    private String imgUrl;
    private String category;
    private String country;
    private String naturalStatus;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private float quantity;
}
