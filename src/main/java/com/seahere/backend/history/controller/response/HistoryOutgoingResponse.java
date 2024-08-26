package com.seahere.backend.history.controller.response;

import com.seahere.backend.outgoing.controller.response.OutgoingDetailResponse;
import com.seahere.backend.outgoing.dto.OutgoingCallDto;
import com.seahere.backend.outgoing.dto.OutgoingDetailDto;
import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.OutgoingState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class HistoryOutgoingResponse {
    private Long outgoingId;
    private Long companyId;
    private String companyName;
    private String customerName;
    private LocalDate outgoingDate;
    private OutgoingState state;
    private String status;
    private boolean partialOutgoing;
    private String title;
    private List<HistoryOutgoingDetailResponse> details;

    public static HistoryOutgoingResponse from(OutgoingEntity outgoingEntity) {

        HistoryOutgoingResponseBuilder historyOutgoingResponse = HistoryOutgoingResponse.builder()
                .companyId(outgoingEntity.getCompany().getId())
                .companyName(outgoingEntity.getCompany().getCompanyName());
                if(outgoingEntity.getTradeType().equals("b2b") && outgoingEntity.getCustomer().getCompany() != null){
                    historyOutgoingResponse.customerName(outgoingEntity.getCustomer().getCompany().getCompanyName());
                }else{
                    historyOutgoingResponse.customerName(outgoingEntity.getCustomer().getUsername());
                }
            historyOutgoingResponse.outgoingDate(outgoingEntity.getOutgoingDate())
                .state(outgoingEntity.getOutgoingState())
                .status(outgoingEntity.getOutgoingState().printState())
                .partialOutgoing(outgoingEntity.isPartialOutgoing())
                .title(calcTitle(outgoingEntity.getOutgoingDetails()))
                .details(outgoingEntity.getOutgoingDetails().stream().map(HistoryOutgoingDetailResponse::from).collect(Collectors.toList()))
                .outgoingId(outgoingEntity.getOutgoingId());
                return historyOutgoingResponse.build();
    }

    private static String calcTitle(List<OutgoingDetailEntity> details){
        if(details.isEmpty()) return "상품이 없습니다.";
        if(details.size() == 1) return details.get(0).getProduct().getProductName() + "외0건";
        return details.get(0).getProduct().getProductName() + "외"+(details.size()-1)+"건";
    }
}
