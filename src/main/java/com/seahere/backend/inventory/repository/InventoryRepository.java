package com.seahere.backend.inventory.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.alarm.entity.QDiscountEntity;
import com.seahere.backend.inventory.controller.request.CustomerInventorySearch;
import com.seahere.backend.inventory.controller.response.InventoryDetailResponse;
import com.seahere.backend.inventory.controller.response.InventoryResponse;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.entity.QInventoryDetailEntity;
import com.seahere.backend.inventory.entity.QInventoryEntity;
import com.seahere.backend.product.dto.ProductDto;
import com.seahere.backend.product.entity.QProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.seahere.backend.company.entity.QCompanyEntity.companyEntity;
import static com.seahere.backend.product.entity.QProductEntity.productEntity;

@Repository
@RequiredArgsConstructor
public class InventoryRepository {

    private final JPAQueryFactory queryFactory;

    private static final QInventoryEntity inventory = QInventoryEntity.inventoryEntity;

    public Slice<InventoryResponse> findPagedInventoryByCompanyId(Long companyId, String search, Pageable pageable) {
        List<InventoryResponse> results = queryFactory
                .select(Projections.constructor(InventoryResponse.class,
                        inventory.company.id,
                        inventory.product.productName.as("name"),
                        inventory.category,
                        inventory.incomingDate.max().as("latestIncoming"),
                        inventory.quantity.sum().as("totalQuantity").floatValue(),
                        inventory.product.productImg.as("productImg")))
                .from(inventory)
                .leftJoin(inventory.company, companyEntity)
                .leftJoin(inventory.product, productEntity)
                .where(inventory.company.id.eq(companyId)
                        .and(inventory.product.productName.containsIgnoreCase(search)))
                .groupBy(inventory.product.productName, inventory.category, inventory.company.id,inventory.product.productImg)
                .orderBy(inventory.product.productName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results = results.subList(0, pageable.getPageSize());
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }

    public Slice<InventoryDetailResponse> findPagedProductsByCompanyId(Long companyId, String name, String category, Pageable pageable) {
        List<InventoryDetailResponse> results = queryFactory
                .select(Projections.constructor(InventoryDetailResponse.class,
                        inventory.inventoryId,
                        inventory.company.id,
                        inventory.product.productName.as("name"),
                        inventory.category,
                        inventory.quantity.floatValue(),
                        inventory.incomingDate,
                        inventory.country,
                        inventory.naturalStatus))
                .from(inventory)
                .leftJoin(inventory.company, companyEntity)
                .leftJoin(inventory.product, productEntity)
                .where(inventory.company.id.eq(companyId)
                        .and(inventory.product.productName.eq(name))
                        .and(inventory.category.eq(category)))
                .orderBy(inventory.incomingDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results = results.subList(0, pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    public List<InventoryEntity> findByCompanyIdWithDetail(Long companyId, CustomerInventorySearch customerInventorySearch) {
        return queryFactory.selectFrom(QInventoryEntity.inventoryEntity)
                .leftJoin(QInventoryEntity.inventoryEntity.product, QProductEntity.productEntity).fetchJoin()
                .leftJoin(QInventoryEntity.inventoryEntity.inventoryDetail, QInventoryDetailEntity.inventoryDetailEntity).fetchJoin()
                .where(
                        QInventoryEntity.inventoryEntity.company.id.eq(companyId)
                                .and(QInventoryEntity.inventoryEntity.quantity.ne(0F))
                )
                .limit(customerInventorySearch.getSize())
                .offset(customerInventorySearch.getOffset())
                .orderBy(QInventoryEntity.inventoryEntity.product.productName.asc(),QInventoryEntity.inventoryEntity.inventoryId.asc())
                .fetch();
    }

    public List<InventoryEntity> findTradeInventory(Long companyId, CustomerInventorySearch customerInventorySearch) {
        return queryFactory.selectFrom(QInventoryEntity.inventoryEntity)
                .leftJoin(QInventoryEntity.inventoryEntity.product, QProductEntity.productEntity).fetchJoin()
                .where(
                        QInventoryEntity.inventoryEntity.company.id.eq(companyId)
                                .and(QInventoryEntity.inventoryEntity.quantity.ne(0F))
                )
                .limit(customerInventorySearch.getSize())
                .offset(customerInventorySearch.getOffset())
                .orderBy(QInventoryEntity.inventoryEntity.product.productName.asc(),QInventoryEntity.inventoryEntity.inventoryId.asc())
                .fetch();
    }

    public List<InventoryEntity> findByCompanyIdWithDetail(Long companyId) {
        return queryFactory.selectFrom(QInventoryEntity.inventoryEntity)
                .leftJoin(QInventoryEntity.inventoryEntity.product, QProductEntity.productEntity).fetchJoin()
                .leftJoin(QInventoryEntity.inventoryEntity.inventoryDetail, QInventoryDetailEntity.inventoryDetailEntity).fetchJoin()
                .leftJoin(QInventoryEntity.inventoryEntity.discount, QDiscountEntity.discountEntity).fetchJoin()
                .where(
                        QInventoryEntity.inventoryEntity.company.id.eq(companyId)
                                .and(QInventoryEntity.inventoryEntity.quantity.ne(0F))
                )
                .fetch();
    }

    public Optional<InventoryEntity> findByIdWithProduct(Long id) {
        InventoryEntity inventory = queryFactory.selectFrom(QInventoryEntity.inventoryEntity)
                .leftJoin(QInventoryEntity.inventoryEntity.product, productEntity).fetchJoin()
                .where(
                        QInventoryEntity.inventoryEntity.inventoryId.eq(id)
                )
                .fetchOne();
        return Optional.ofNullable(inventory);
    }

    public List<ProductDto> findAllDistinctProductNamesByCompanyId(Long companyId) {
        return queryFactory
                .select(Projections.constructor(ProductDto.class,
                        productEntity.productId,
                        productEntity.productName,
                        productEntity.qr,
                        productEntity.productImg))
                .distinct()
                .from(inventory)
                .leftJoin(inventory.product, productEntity)
                .where(inventory.company.id.eq(companyId))
                .fetch();
    }
}
