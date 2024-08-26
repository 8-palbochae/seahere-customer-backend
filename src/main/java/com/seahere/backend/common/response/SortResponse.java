package com.seahere.backend.common.response;

import lombok.Getter;
import org.springframework.data.domain.Sort;
@Getter
public class SortResponse {
    boolean empty;
    boolean sorted;
    boolean unsorted;

    public SortResponse(Sort sort) {
        this.empty = sort.isEmpty();
        this.sorted = sort.isSorted();
        this.unsorted = sort.isUnsorted();
    }
}
