package com.seahere.backend.outgoing.entity;

import com.seahere.backend.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "outgoing_detail")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class OutgoingDetailEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outgoing_id")
    private OutgoingEntity outgoing;

    private BigDecimal price;

    private float quantity;

    private String country;

    private String naturalStatus;

    private String category;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'ACTIVE'")
    private OutgoingDetailState state;

    public void assignOutgoing(OutgoingEntity outgoingEntity){
        this.outgoing = outgoingEntity;
    }

    public void stateToDelete(){
        this.state = OutgoingDetailState.DELETE;
    }
    public void stateToActive(){
        this.state = OutgoingDetailState.ACTIVE;
    }

    public boolean isPossibleDelete(){
        return this.outgoing.isPartialOutgoing();
    }

    public boolean isLackInventory(float inventoryQuantity){
        return inventoryQuantity < this.quantity;
    }

    public boolean isNotDelete(){
        return this.state != OutgoingDetailState.DELETE;
    }
    public boolean isDelete(){
        return this.state == OutgoingDetailState.DELETE;
    }
}
