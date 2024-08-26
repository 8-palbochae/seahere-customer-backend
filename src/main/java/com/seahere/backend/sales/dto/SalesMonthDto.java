package com.seahere.backend.sales.dto;

import lombok.*;


@Getter
@ToString
@Builder
@NoArgsConstructor
public class SalesMonthDto {

    private int commonPrice;
    private int month;

    public SalesMonthDto(int month, int incomingPrice) {
        this.commonPrice = incomingPrice;
        this.month = month;
    }
}
