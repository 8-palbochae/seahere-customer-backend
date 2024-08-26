package com.seahere.backend.outgoing.entity;

import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.user.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outgoing")
@NoArgsConstructor
@Getter
public class OutgoingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outgoingId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "outgoing", cascade = CascadeType.ALL)
    private final List<OutgoingDetailEntity> outgoingDetails = new ArrayList<>();

    private LocalDate outgoingDate;

    private String tradeType;

    @Enumerated(EnumType.STRING)
    private OutgoingState outgoingState;

    private boolean partialOutgoing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity customer;

    @Builder
    public OutgoingEntity(CompanyEntity company,UserEntity customer, LocalDate outgoingDate, OutgoingState outgoingState, boolean partialOutgoing, String tradeType) {
        this.company = company;
        this.customer = customer;
        this.outgoingDate = outgoingDate;
        this.outgoingState = outgoingState;
        this.partialOutgoing = partialOutgoing;
        this.tradeType = tradeType;
    }

    public void addOutgoingDetail(OutgoingDetailEntity outgoingDetail){
        this.outgoingDetails.add(outgoingDetail);
        outgoingDetail.assignOutgoing(this);
    }

    public void changeState(OutgoingState state){
        this.outgoingState = state;
    }
}
