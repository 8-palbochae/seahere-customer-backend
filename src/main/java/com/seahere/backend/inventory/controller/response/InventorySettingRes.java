package com.seahere.backend.inventory.controller.response;

import com.seahere.backend.inventory.entity.InventoryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class InventorySettingRes {
    private Long id;
    private int warningQuantity;
    private BigDecimal outgoingPrice;
    private Long inventoryId;
    private String name;
    private String imgUrl;
    private String category;
    private String country;
    private String naturalStatus;

    @Builder
    public InventorySettingRes(Long id, int warningQuantity, BigDecimal outgoingPrice) {
        this.id = id;
        this.warningQuantity = warningQuantity;
        this.outgoingPrice = outgoingPrice;
    }

    public InventorySettingRes (InventoryEntity entity){
        this.id = entity.getInventoryDetail().getId();
        this.outgoingPrice = entity.getInventoryDetail().getOutgoingPrice();
        this.category = entity.getCategory();
        this.country = entity.getCountry();
        this.name = entity.getProduct().getProductName();
        this.imgUrl = entity.getProduct().getProductImg();
        this.inventoryId = entity.getInventoryId();
        this.naturalStatus = entity.getNaturalStatus();
        this.warningQuantity = entity.getInventoryDetail().getWarningQuantity();
    }
}
