package com.seahere.backend.inventory.controller.response;

import com.seahere.backend.inventory.entity.InventoryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class InventoryTradeRes {

    private Long inventoryId;
    private String name;
    private String imgUrl;
    private String category;
    private String country;
    private String naturalStatus;
    private float quantity;

    @Builder
    public InventoryTradeRes(Long inventoryId, String name, String imgUrl, String category, String country, String naturalStatus, float quantity) {
        this.inventoryId = inventoryId;
        this.name = name;
        this.imgUrl = imgUrl;
        this.category = category;
        this.country = country;
        this.naturalStatus = naturalStatus;
        this.quantity = quantity;
    }

    public static InventoryTradeRes from(InventoryEntity inventory){
        return InventoryTradeRes.builder()
                .inventoryId(inventory.getInventoryId())
                .name(inventory.getProduct().getProductName())
                .imgUrl(inventory.getProduct().getProductImg())
                .country(inventory.getCountry())
                .category(inventory.getCategory())
                .naturalStatus(inventory.getNaturalStatus())
                .quantity(inventory.getQuantity())
                .build();
    }

}
