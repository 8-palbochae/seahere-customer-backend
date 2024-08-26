package com.seahere.backend.outgoing.controller.response;

import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CustomerOutgoingDetailRes {
    private Long outgoingDetailId;
    private String productImg;
    private String productName;
    private String naturalStatus;
    private String country;
    private String category;
    private LocalDate outgoingDate;
    private float outgoingQuantity;
    private BigDecimal price;

    @Builder
    public CustomerOutgoingDetailRes(Long outgoingDetailId, String productImg, String productName, String naturalStatus, String country, String category, LocalDate outgoingDate, float outgoingQuantity, BigDecimal price) {
        this.outgoingDetailId = outgoingDetailId;
        this.productImg = productImg;
        this.productName = productName;
        this.naturalStatus = naturalStatus;
        this.country = country;
        this.category = category;
        this.outgoingDate = outgoingDate;
        this.outgoingQuantity = outgoingQuantity;
        this.price = price;
    }

    public static CustomerOutgoingDetailRes from(OutgoingDetailEntity outgoingDetail){
        return CustomerOutgoingDetailRes.builder()
                .outgoingDetailId(outgoingDetail.getDetailId())
                .outgoingDate(outgoingDetail.getOutgoing().getOutgoingDate())
                .productName(outgoingDetail.getProduct().getProductName())
                .category(outgoingDetail.getCategory())
                .country(outgoingDetail.getCountry())
                .naturalStatus(outgoingDetail.getNaturalStatus())
                .price(outgoingDetail.getPrice())
                .outgoingQuantity(outgoingDetail.getQuantity())
                .productImg(outgoingDetail.getProduct().getProductImg())
                .build();
    }
}
