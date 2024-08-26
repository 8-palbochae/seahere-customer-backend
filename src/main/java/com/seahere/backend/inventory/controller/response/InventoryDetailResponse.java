package com.seahere.backend.inventory.controller.response;

import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDetailResponse {
    private Long inventoryId;
    private Long companyId;
    private String name;
    private String category;
    private float quantity;
    private LocalDate incomingDate;
    private String country;
    private String naturalStatus;
}
