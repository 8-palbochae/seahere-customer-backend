package com.seahere.backend.outgoing.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Setter
@NoArgsConstructor
@ToString
public class OutgoingDetailDto {
    private Long outgoingId;
    private Long outgoingDetailId;
    private String imgSrc;
    private String productName;
    private String country;
    private String naturalStatus;
    private String category;
    private float outgoingQuantity;
    private BigDecimal price;
    private float inventoryQuantity;
    private LocalDate outgoingDate;
}
