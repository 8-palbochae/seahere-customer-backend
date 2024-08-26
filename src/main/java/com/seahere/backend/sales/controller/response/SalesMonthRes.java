package com.seahere.backend.sales.controller.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SalesMonthRes {

    private int month;
    private int commonPrice;

}
