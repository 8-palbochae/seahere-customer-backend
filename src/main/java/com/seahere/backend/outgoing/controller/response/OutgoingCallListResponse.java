package com.seahere.backend.outgoing.controller.response;

import com.seahere.backend.common.response.SortResponse;
import com.seahere.backend.outgoing.dto.OutgoingCallDto;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;
@Getter
public class OutgoingCallListResponse {
    private List<OutgoingCallDto> content;
    private final SortResponse sort;
    private final int currentPage;
    private final int size;
    private final boolean first;
    private final boolean last;
    private final boolean hasNext;

    public OutgoingCallListResponse(Slice<OutgoingEntity> slice) {
        this.content = slice.getContent().stream().map(OutgoingCallDto::from).collect(Collectors.toList());
        this.sort = new SortResponse(slice.getSort());
        this.currentPage = slice.getNumber();
        this.size = slice.getSize();
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.hasNext = slice.hasNext();
    }
}
