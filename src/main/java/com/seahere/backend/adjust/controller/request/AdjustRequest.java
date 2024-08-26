package com.seahere.backend.adjust.controller.request;

import com.seahere.backend.adjust.entity.AdjustEntity;
import com.seahere.backend.inventory.entity.InventoryEntity;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdjustRequest {
    private Long inventoryId;
    private String reason;
    private float afterQuantity;

    public AdjustEntity toEntity(InventoryEntity inventoryEntity){
        return AdjustEntity.builder()
                .adjustDate(LocalDate.now())
                .reason(this.reason)
                .beforeQuantity(inventoryEntity.getQuantity())
                .afterQuantity(this.afterQuantity)
                .inventory(inventoryEntity)
                .build();
    }
}
