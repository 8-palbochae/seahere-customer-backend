package com.seahere.backend.history.controller.response;

import com.seahere.backend.common.response.SortResponse;
import com.seahere.backend.history.dto.HistoryListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryListResponse {
    private List<HistoryResponse> content;
    private SortResponse sort;
    private int currentPage;
    private int size;
    private boolean first;
    private boolean last;
    private boolean hasNext;

    public HistoryListResponse(Slice<HistoryListDto> slice) {
        this.content = slice.getContent().stream().map(HistoryResponse::from).collect(Collectors.toList());
        this.sort = new SortResponse(slice.getSort());
        this.currentPage = slice.getNumber();
        this.size = slice.getSize();
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.hasNext = slice.hasNext();
    }
}
