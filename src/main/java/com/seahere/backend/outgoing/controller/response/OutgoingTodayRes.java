package com.seahere.backend.outgoing.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OutgoingTodayRes {
    private long pending;
    private long ready;
    private long complete;

    @Builder
    public OutgoingTodayRes(long pending, long ready, long complete) {
        this.pending = pending;
        this.ready = ready;
        this.complete = complete;
    }
}
