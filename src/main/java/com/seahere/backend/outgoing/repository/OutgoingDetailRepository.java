package com.seahere.backend.outgoing.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.inventory.entity.QInventoryDetailEntity;
import com.seahere.backend.inventory.entity.QInventoryEntity;
import com.seahere.backend.outgoing.dto.OutgoingDetailDto;
import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingDetailState;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.QOutgoingDetailEntity;
import com.seahere.backend.product.entity.QProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.seahere.backend.company.entity.QCompanyEntity.companyEntity;
import static com.seahere.backend.inventory.entity.QInventoryEntity.inventoryEntity;
import static com.seahere.backend.outgoing.entity.QOutgoingDetailEntity.outgoingDetailEntity;
import static com.seahere.backend.outgoing.entity.QOutgoingEntity.outgoingEntity;
import static com.seahere.backend.product.entity.QProductEntity.productEntity;

@Repository
@RequiredArgsConstructor
public class OutgoingDetailRepository {

    private final JPAQueryFactory queryFactory;

    public List<OutgoingDetailDto> findByOutgoingAndStateActive(Long outgoingId) {
        return queryFactory.select(Projections.bean(OutgoingDetailDto.class
                        , outgoingEntity.outgoingId.as("outgoingId")
                        , outgoingDetailEntity.detailId.as("outgoingDetailId")
                        , productEntity.productImg.as("imgSrc")
                        , productEntity.productName.as("productName")
                        , outgoingDetailEntity.quantity.as("outgoingQuantity")
                        , outgoingDetailEntity.price.as("price")
                        , inventoryEntity.quantity.as("inventoryQuantity")
                        , outgoingDetailEntity.category.as("category")
                        , outgoingDetailEntity.country.as("country")
                        , outgoingDetailEntity.naturalStatus.as("naturalStatus")
                        , outgoingEntity.outgoingDate.as("outgoingDate")
                )).distinct()
                .from(outgoingDetailEntity)
                .leftJoin(outgoingDetailEntity.outgoing, outgoingEntity)
                .leftJoin(outgoingDetailEntity.product, productEntity)
                .leftJoin(outgoingEntity.company, companyEntity)
                .leftJoin(inventoryEntity).on(inventoryEntity.company.eq(companyEntity)
                        .and(inventoryEntity.product.eq(outgoingDetailEntity.product))
                        .and(inventoryEntity.category.eq(outgoingDetailEntity.category))
                        .and(inventoryEntity.naturalStatus.eq(outgoingDetailEntity.naturalStatus))
                        .and(inventoryEntity.country.eq(outgoingDetailEntity.country)))
                .where(outgoingEntity.outgoingId.eq(outgoingId)
                        .and(outgoingDetailEntity.state.eq(OutgoingDetailState.ACTIVE)))
                .fetch();
    }

    public List<OutgoingDetailEntity> findByOutgoingId(Long outgoingId){
        return queryFactory.selectFrom(outgoingDetailEntity)
                .leftJoin(outgoingDetailEntity.product, productEntity).fetchJoin()
                .where(
                        outgoingDetailEntity.outgoing.outgoingId.eq(outgoingId)
                )
                .fetch();
    }
}
