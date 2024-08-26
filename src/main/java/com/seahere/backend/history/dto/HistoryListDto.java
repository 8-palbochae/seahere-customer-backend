package com.seahere.backend.history.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class HistoryListDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate date;
    private long incomingCount;
    private long outgoingCount;
    private long adjustCount;

    @Builder
    public HistoryListDto(String date, long incomingCount, long outgoingCount, long adjustCount) {
        this.date = LocalDate.parse(date);
        this.incomingCount = incomingCount;
        this.outgoingCount = outgoingCount;
        this.adjustCount = adjustCount;
    }
}
