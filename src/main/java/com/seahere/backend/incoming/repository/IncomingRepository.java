package com.seahere.backend.incoming.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.incoming.dto.IncomingCountDto;
import com.seahere.backend.incoming.entity.IncomingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.seahere.backend.company.entity.QCompanyEntity.companyEntity;
import static com.seahere.backend.incoming.entity.QIncomingEntity.incomingEntity;
import static com.seahere.backend.product.entity.QProductEntity.productEntity;

@Repository
@RequiredArgsConstructor
public class IncomingRepository {

    private final JPAQueryFactory queryFactory;

    public List<IncomingCountDto> findIncomingCountList(Long companyId, LocalDate startDate, LocalDate endDate){
        return queryFactory.select(Projections.constructor(IncomingCountDto.class, incomingEntity.incomingDate, incomingEntity.incomingId.count()))
                .from(incomingEntity)
                .leftJoin(incomingEntity.company, companyEntity)
                .leftJoin(incomingEntity.product, productEntity)
                .where(incomingPeriodFindList(companyId, startDate, endDate))
                .groupBy(incomingEntity.incomingDate)
                .fetch();


    }

    private BooleanExpression incomingPeriodFindList(Long companyId,LocalDate startDate, LocalDate endDate){
        return incomingEntity.company.id.eq(companyId)
                .and(incomingEntity.incomingDate.goe(startDate))
                .and(incomingEntity.incomingDate.loe(endDate));
    }
    public List<IncomingEntity> findIncomingList(Long companyId, LocalDate incomingDate){
        return queryFactory.selectFrom(incomingEntity)
                .leftJoin(incomingEntity.company, companyEntity).fetchJoin()
                .leftJoin(incomingEntity.product, productEntity).fetchJoin()
                .where(incomingEntity.company.id.eq(companyId).and(incomingEntity.incomingDate.eq(incomingDate)))
                .fetch();


    }



}