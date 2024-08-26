package com.seahere.backend.outgoing.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class OutgoingCreateReq {
    private Long companyId;
    private boolean partialOutgoing;
    private String tradeType;
    private List<OutgoingCreateDetailReq> details;

    @Builder
    public OutgoingCreateReq(Long companyId, boolean partialOutgoing, String tradeType, List<OutgoingCreateDetailReq> details) {
        this.companyId = companyId;
        this.partialOutgoing = partialOutgoing;
        this.tradeType = tradeType;
        this.details = details;
    }
}
