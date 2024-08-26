package com.seahere.backend.incoming.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@ToString
public class IncomingEditReq {
    private Long incomingId;

    @NotNull(message = "가격은 항상 있어야 합니다.")
    private Integer price;

    @Builder
    public IncomingEditReq(Long incomingId, Integer price) {
        this.incomingId = incomingId;
        this.price = price;
    }
}