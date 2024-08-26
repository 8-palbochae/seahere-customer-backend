package com.seahere.backend.inventory.entity;

import com.seahere.backend.common.entity.ProductCountry;
import com.seahere.backend.common.entity.ProductStatus;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.inventory.controller.request.InventoryEditReq;
import com.seahere.backend.inventory.entity.InventoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="inventory_detail")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class InventoryDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_detail_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "inventory_id")
    private InventoryEntity inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private int warningQuantity;

    private BigDecimal outgoingPrice;

    public void edit(InventoryEditReq inventoryEditReq){
        this.warningQuantity = inventoryEditReq.getWarningQuantity() != null ? inventoryEditReq.getWarningQuantity() : warningQuantity;
        this.outgoingPrice = inventoryEditReq.getOutgoingPrice() != null ? inventoryEditReq.getOutgoingPrice() : outgoingPrice;
    }

    public void changePrice(BigDecimal price){
        this.outgoingPrice = price;
    }
}
