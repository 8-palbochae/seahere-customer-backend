package com.seahere.backend.history.controller.response;

import com.seahere.backend.history.dto.HistoryListDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistoryResponse {
    LocalDate date;
    private long incomingCount;
    private long outgoingCount;
    private long adjustCount;

    @Builder
    public HistoryResponse(LocalDate date, long incomingCount, long outgoingCount, long adjustCount) {
        this.date = date;
        this.incomingCount = incomingCount;
        this.outgoingCount = outgoingCount;
        this.adjustCount = adjustCount;
    }

    public static HistoryResponse from(HistoryListDto historyListDto){
        return HistoryResponse.builder()
                .date(historyListDto.getDate())
                .incomingCount(historyListDto.getIncomingCount())
                .adjustCount(historyListDto.getAdjustCount())
                .outgoingCount(historyListDto.getOutgoingCount())
                .build();
    }
}
