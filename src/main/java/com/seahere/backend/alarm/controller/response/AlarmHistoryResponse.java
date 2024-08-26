package com.seahere.backend.alarm.controller.response;

import com.seahere.backend.alarm.dto.AlarmHistoryDto;
import com.seahere.backend.alarm.entity.AlarmHistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class AlarmHistoryResponse {
    private final List<AlarmHistoryDto> content;
    private final int currentPage;
    private final int size;
    private final boolean first;
    private final boolean last;
    private final boolean hasNext;

    public AlarmHistoryResponse (Slice<AlarmHistoryEntity> slice){
        this.content = slice.getContent().stream().map(AlarmHistoryDto::from).collect(Collectors.toList());
        this.currentPage = slice.getNumber();
        this.size = slice.getSize();
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.hasNext = slice.hasNext();
    }
}
