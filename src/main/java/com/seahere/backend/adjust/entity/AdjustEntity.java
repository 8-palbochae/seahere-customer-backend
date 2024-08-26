package com.seahere.backend.adjust.entity;

import com.seahere.backend.inventory.entity.InventoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "adjust")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdjustEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjust_id")
    private Long adjustId;

    @Column(name = "adjust_date")
    private LocalDate adjustDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "before_quantity")
    private float beforeQuantity;

    @Column(name = "after_quantity")
    private float afterQuantity;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntity inventory;
}