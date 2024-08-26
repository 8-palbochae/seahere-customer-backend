package com.seahere.backend.inventory.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class InventoryEditReq {
    private BigDecimal outgoingPrice;
    private Integer warningQuantity;

    @Builder
    public InventoryEditReq(BigDecimal outgoingPrice, Integer warningQuantity) {
        this.outgoingPrice = outgoingPrice;
        this.warningQuantity = warningQuantity;
    }
}
