package com.seahere.backend.inventory.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReqDetailSearchRequest {
    private int size;
    private int page;
    private String name;
    private String category;
}
