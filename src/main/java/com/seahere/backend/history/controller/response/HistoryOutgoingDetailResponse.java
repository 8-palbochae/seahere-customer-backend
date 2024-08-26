package com.seahere.backend.history.controller.response;

import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class HistoryOutgoingDetailResponse {
    private Long outgoingId;
    private Long outgoingDetailId;
    private String productImg;
    private String productName;
    private float outgoingQuantity;
    private int price;
    private String category;
    private String country;
    private String naturalStatus;
    private boolean isDelete;
    public static HistoryOutgoingDetailResponse from(OutgoingDetailEntity outgoingDetail){
        BigDecimal priceValue = outgoingDetail.getPrice() != null ? outgoingDetail.getPrice() : BigDecimal.ZERO;
        return HistoryOutgoingDetailResponse.builder()
                .productImg(outgoingDetail.getProduct().getProductImg())
                .outgoingId(outgoingDetail.getOutgoing().getOutgoingId())
                .price(priceValue.intValue())
                .category(outgoingDetail.getCategory())
                .country(outgoingDetail.getCountry())
                .naturalStatus(outgoingDetail.getNaturalStatus())
                .outgoingQuantity(outgoingDetail.getQuantity())
                .productName(outgoingDetail.getProduct().getProductName())
                .isDelete(outgoingDetail.isDelete())
                .outgoingDetailId(outgoingDetail.getDetailId()).build();
    }
}
