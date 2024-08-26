package com.seahere.backend.adjust.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.adjust.entity.AdjustEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.seahere.backend.adjust.entity.QAdjustEntity.adjustEntity;
import static com.seahere.backend.company.entity.QCompanyEntity.companyEntity;
import static com.seahere.backend.inventory.entity.QInventoryEntity.inventoryEntity;
import static com.seahere.backend.product.entity.QProductEntity.productEntity;

@Repository
@RequiredArgsConstructor
public class AdjustRepository {

    private final JPAQueryFactory queryFactory;
    public List<AdjustEntity> findByCompanyIdAndDate(long companyId, LocalDate date) {
        return queryFactory.selectFrom(adjustEntity)
                .leftJoin(adjustEntity.inventory, inventoryEntity).fetchJoin()
                .leftJoin(inventoryEntity.company, companyEntity).fetchJoin()
                .leftJoin(inventoryEntity.product, productEntity).fetchJoin()
                .where(adjustEntity.adjustDate.eq(date).and(companyEntity.id.eq(companyId)))
                .fetch();
    }
}
