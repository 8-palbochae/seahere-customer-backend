package com.seahere.backend.sales.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PeriodRequest {

    private LocalDate startDate;
    private LocalDate endDate;

}
