package com.seahere.backend.outgoing.controller.response;

import com.seahere.backend.company.controller.response.CompanyResponse;
import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class OutgoingRes {
    private CompanyResponse company;
    private Long outgoingId;
    private String title;
    private String state;
    private BigDecimal price;
    private LocalDate outgoingDate;

    @Builder
    public OutgoingRes(CompanyResponse company, Long outgoingId, String title, String state, BigDecimal price, LocalDate outgoingDate) {
        this.company = company;
        this.outgoingId = outgoingId;
        this.title = title;
        this.state = state;
        this.price = price;
        this.outgoingDate = outgoingDate;
    }

    public static OutgoingRes from(OutgoingEntity outgoingEntity){
        String outgoingTitle = outgoingEntity.getOutgoingDetails().get(0).getProduct().getProductName() 
                + " 외" + (outgoingEntity.getOutgoingDetails().size()-1)
                + "개";

        if (outgoingEntity.getOutgoingDetails().size() <=1){
            outgoingTitle = outgoingEntity.getOutgoingDetails().get(0).getProduct().getProductName();
        }

        BigDecimal price = outgoingEntity.getOutgoingDetails().stream()
                .map(OutgoingDetailEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OutgoingRes.builder()
                .company(CompanyResponse.from(outgoingEntity.getCompany()))
                .outgoingId(outgoingEntity.getOutgoingId())
                .outgoingDate(outgoingEntity.getOutgoingDate())
                .state(outgoingEntity.getOutgoingState().toString())
                .title(outgoingTitle)
                .price(price)
                .build();
    }

}
