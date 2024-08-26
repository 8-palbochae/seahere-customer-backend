package com.seahere.backend.incoming.controller.request;

import com.seahere.backend.incoming.entity.IncomingEntity;
import com.seahere.backend.product.entity.ProductEntity;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IncomingDataRequest {
    private Long productId;
    private float quantity;
    private int incomingPrice;
    private String memo;
    private String countryDetail;
    private String natural;
    private String category;
    private String country;

    public IncomingEntity toEntity(ProductEntity product) {
        return IncomingEntity.builder()
                .product(product)
                .quantity(this.quantity)
                .incomingDate(LocalDate.now())
                .incomingPrice(this.incomingPrice)
                .memo(this.memo)
                .category(this.category)
                .country(this.country)
                .countryDetail(this.countryDetail)
                .naturalStatus(this.natural)
                .build();
    }
}
