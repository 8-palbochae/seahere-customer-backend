package com.seahere.backend.sales.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class SalesWeekRes {
    private LocalDate commonDate;
    private int commonPrice;
    private  int week;

}
