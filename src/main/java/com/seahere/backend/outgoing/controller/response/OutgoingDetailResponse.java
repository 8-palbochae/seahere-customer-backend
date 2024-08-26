package com.seahere.backend.outgoing.controller.response;

import com.seahere.backend.outgoing.dto.OutgoingDetailDto;
import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class OutgoingDetailResponse {
    private Long outgoingId;
    private Long outgoingDetailId;
    private String productImg;
    private String productName;
    private String naturalStatus;
    private String country;
    private String category;
    private LocalDate outgoingDate;
    private float outgoingQuantity;
    private float beforeCount;
    private float afterCount;
    private int price;

    public static OutgoingDetailResponse from(OutgoingDetailDto outgoingDetailDto){
        BigDecimal priceValue = outgoingDetailDto.getPrice() != null ? outgoingDetailDto.getPrice() : BigDecimal.ZERO;

        return OutgoingDetailResponse.builder()
                .productImg(outgoingDetailDto.getImgSrc())
                .outgoingId(outgoingDetailDto.getOutgoingId())
                .outgoingDetailId(outgoingDetailDto.getOutgoingDetailId())
                .outgoingQuantity(outgoingDetailDto.getOutgoingQuantity())
                .beforeCount(outgoingDetailDto.getInventoryQuantity())
                .afterCount(outgoingDetailDto.getInventoryQuantity() - outgoingDetailDto.getOutgoingQuantity())
                .price(priceValue.intValue())
                .productName(outgoingDetailDto.getProductName())
                .naturalStatus(outgoingDetailDto.getNaturalStatus())
                .country(outgoingDetailDto.getCountry())
                .category(outgoingDetailDto.getCategory())
                .outgoingDate(outgoingDetailDto.getOutgoingDate())
                .build();
    }
}
