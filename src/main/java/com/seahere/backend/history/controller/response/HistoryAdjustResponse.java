package com.seahere.backend.history.controller.response;

import com.seahere.backend.adjust.entity.AdjustEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@Builder
public class HistoryAdjustResponse {
    private Long adjustId;
    private LocalDate adjustDate;
    private String productImg;
    private String productName;
    private String reason;
    private String category;
    private String naturalStatus;
    private String country;
    private float beforeQuantity;
    private float afterQuantity;

    public static HistoryAdjustResponse from(AdjustEntity adjust){
        return HistoryAdjustResponse.builder()
                .adjustId(adjust.getAdjustId())
                .adjustDate(adjust.getAdjustDate())
                .category(adjust.getInventory().getCategory())
                .naturalStatus(adjust.getInventory().getNaturalStatus())
                .country(adjust.getInventory().getCountry())
                .afterQuantity(adjust.getAfterQuantity())
                .beforeQuantity(adjust.getBeforeQuantity())
                .productImg(adjust.getInventory().getProduct().getProductImg())
                .productName(adjust.getInventory().getProduct().getProductName())
                .reason(adjust.getReason())
                .build();
    }
}
