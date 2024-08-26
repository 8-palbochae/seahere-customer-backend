package com.seahere.backend.inventory.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReqUpdateRequest {
    private Long companyId;
    private Long inventoryId;
    private int size;
    private int page;
    private String search;
}
