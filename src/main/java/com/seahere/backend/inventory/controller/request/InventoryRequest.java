package com.seahere.backend.inventory.controller.request;

import com.seahere.backend.inventory.entity.InventoryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@Getter
@ToString
public class InventoryRequest {
    private final Long inventoryId;
    private final Long companyId;
    private final float quantity;
    private final String category;
    private final String name;
    private final String country;
    private final LocalDate incomingDate;
    private final String naturalStatus;

    public InventoryEntity toEntity(){
        return InventoryEntity.builder()
                .inventoryId(this.inventoryId)
                .quantity(this.quantity)
                .category(this.category)
                .country(this.country)
                .incomingDate(this.incomingDate)
                .naturalStatus(this.naturalStatus).build();
    }

}
