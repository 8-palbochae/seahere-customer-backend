package com.seahere.backend.outgoing.dto;

import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.OutgoingState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OutgoingCallDto {

    private Long outgoingId;
    private Long companyId;
    private String companyName;
    private String customerName;
    private LocalDate outgoingDate;
    private OutgoingState state;
    private String status;
    private boolean partialOutgoing;
    private String title;


    public static OutgoingCallDto from(OutgoingEntity outgoingEntity) {
        OutgoingCallDtoBuilder outgoingCallDto = OutgoingCallDto.builder()
                .companyId(outgoingEntity.getCompany().getId())
                .companyName(outgoingEntity.getCompany().getCompanyName());
                    if(outgoingEntity.getTradeType().equals("b2b") && outgoingEntity.getCustomer().getCompany() != null){
                        outgoingCallDto.customerName(outgoingEntity.getCustomer().getCompany().getCompanyName());
                    }else{
                    outgoingCallDto.customerName(outgoingEntity.getCustomer().getUsername());
                    }
                outgoingCallDto.outgoingDate(outgoingEntity.getOutgoingDate())
                .state(outgoingEntity.getOutgoingState())
                .status(outgoingEntity.getOutgoingState().printState())
                .partialOutgoing(outgoingEntity.isPartialOutgoing())
                .title(calcTitle(outgoingEntity.getOutgoingDetails()))
                .outgoingId(outgoingEntity.getOutgoingId());
                return outgoingCallDto.build();
    }

    private static String calcTitle(List<OutgoingDetailEntity> details){
        if(details.isEmpty()) return "상품이 없습니다.";
        if(details.size() == 1) return details.get(0).getProduct().getProductName() + "외0건";
        return details.get(0).getProduct().getProductName() + "외"+(details.size()-1)+"건";
    }
}