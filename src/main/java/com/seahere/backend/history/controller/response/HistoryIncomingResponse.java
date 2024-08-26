package com.seahere.backend.history.controller.response;

import com.seahere.backend.incoming.entity.IncomingEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class HistoryIncomingResponse {
    private Long incomingId;
    private Long companyId;
    private String productName;
    private float quantity;
    private LocalDate incomingDate;
    private int incomingPrice;
    private String memo;
    private String countryDetail;
    private String country;
    private String naturalStatus;
    private String category;
    private Long userId;
    private String productImg;

    @Builder
    public HistoryIncomingResponse(Long incomingId, Long companyId, String productName, float quantity, LocalDate incomingDate, int incomingPrice, String memo, String countryDetail, String country, String naturalStatus, String category, Long userId, String productImg) {
        this.incomingId = incomingId;
        this.companyId = companyId;
        this.productName = productName;
        this.quantity = quantity;
        this.incomingDate = incomingDate;
        this.incomingPrice = incomingPrice;
        this.memo = memo;
        this.countryDetail = countryDetail;
        this.country = country;
        this.naturalStatus = naturalStatus;
        this.category = category;
        this.userId = userId;
        this.productImg = productImg;
    }

    public static HistoryIncomingResponse from(IncomingEntity incomingEntity) {
        return HistoryIncomingResponse.builder()
                .companyId(incomingEntity.getCompany().getId())
                .incomingId(incomingEntity.getIncomingId())
                .productName(incomingEntity.getProduct().getProductName())
                .category(incomingEntity.getCategory())
                .country(incomingEntity.getCountry())
                .naturalStatus(incomingEntity.getNaturalStatus())
                .quantity(incomingEntity.getQuantity())
                .incomingPrice(incomingEntity.getIncomingPrice())
                .incomingDate(incomingEntity.getIncomingDate())
                .memo(incomingEntity.getMemo())
                .productImg(incomingEntity.getProduct().getProductImg())
                .build();
    }
}
