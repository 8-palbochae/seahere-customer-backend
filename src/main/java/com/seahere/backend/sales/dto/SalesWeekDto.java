package com.seahere.backend.sales.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@AllArgsConstructor
@Getter
@ToString
@Builder
@NoArgsConstructor
@Slf4j
public class SalesWeekDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate commonDate;
    private int week;
    private int commonPrice;

    public SalesWeekDto(String incomingDate, Integer week, Integer incomingPrice) {

        this.commonDate = LocalDate.parse(incomingDate);
        this.week = week;
        this.commonPrice = incomingPrice;
    }
}

