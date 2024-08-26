package com.seahere.backend.outgoing.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.outgoing.controller.request.OutgoingSearchReq;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.OutgoingState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.seahere.backend.company.entity.QCompanyEntity.companyEntity;
import static com.seahere.backend.outgoing.entity.QOutgoingDetailEntity.outgoingDetailEntity;
import static com.seahere.backend.outgoing.entity.QOutgoingEntity.outgoingEntity;
import static com.seahere.backend.product.entity.QProductEntity.productEntity;
import static com.seahere.backend.user.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class OutgoingRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<OutgoingEntity> findByOutgoingStateIsPending(Long companyId, Pageable pageable, LocalDate startDate, LocalDate endDate, String search) {
        List<OutgoingEntity> results = queryFactory.selectFrom(outgoingEntity).distinct()
                .leftJoin(outgoingEntity.outgoingDetails, outgoingDetailEntity)
                .leftJoin(outgoingEntity.company,companyEntity).fetchJoin()
                .leftJoin(outgoingEntity.customer, userEntity).fetchJoin()
                .leftJoin(outgoingDetailEntity.product,productEntity)
                .where(outgoingStateIsPending(companyId,startDate,endDate,search))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        boolean hasNext = results.size() > pageable.getPageSize();
        if(hasNext){
            results.remove(results.size() - 1);
        }
        return new SliceImpl<>(results,pageable, hasNext);
    }

    public List<OutgoingEntity> findByOutgoingByCustomerId(OutgoingSearchReq searchReq, Long customerId){
        return queryFactory.selectFrom(outgoingEntity).distinct()
                .leftJoin(outgoingEntity.outgoingDetails, outgoingDetailEntity)
                .leftJoin(outgoingEntity.company,companyEntity).fetchJoin()
                .leftJoin(outgoingEntity.customer, userEntity).fetchJoin()
                .leftJoin(outgoingDetailEntity.product,productEntity)
                .where(outgoingEntity.customer.id.eq(customerId))
                .orderBy(outgoingEntity.outgoingId.desc())
                .offset(searchReq.getOffset())
                .limit(searchReq.getSize())
                .fetch();
    }

    public OutgoingEntity findByCustomerRecently(Long customerId){
        return queryFactory.selectFrom(outgoingEntity)
                .where(outgoingEntity.customer.id.eq(customerId))
                .orderBy(outgoingEntity.outgoingDate.desc())
                .limit(1)
                .fetchOne();
    }


    private BooleanExpression outgoingStateIsPending(Long companyId,LocalDate startDate, LocalDate endDate, String search) {
        return outgoingEntity.outgoingState.eq(OutgoingState.PENDING)
                .and(outgoingEntity.company.id.eq(companyId))
                .and(outgoingEntity.outgoingDate.goe(startDate))
                .and(outgoingEntity.outgoingDate.loe(endDate))
                .and(userEntity.username.contains(search).or(productEntity.productName.contains(search)));
    }

    public List<OutgoingEntity> findByOutgoingStateIsNotPendingAndDate(Long companyId, LocalDate date,String search){
        return queryFactory.selectFrom(outgoingEntity).distinct()
                .leftJoin(outgoingEntity.outgoingDetails, outgoingDetailEntity)
                .leftJoin(outgoingEntity.customer, userEntity).fetchJoin()
                .leftJoin(outgoingEntity.company, companyEntity).fetchJoin()
                .leftJoin(outgoingDetailEntity.product,productEntity)
                .where(outgoingStateIsNotPendingAndDate(companyId,date,search))
                .fetch();
    }
    private BooleanExpression outgoingStateIsNotPendingAndDate(Long companyId, LocalDate date, String search) {
        return outgoingEntity.outgoingState.ne(OutgoingState.PENDING)
                .and(outgoingEntity.company.id.eq(companyId))
                .and(outgoingEntity.outgoingDate.eq(date))
                .and(userEntity.username.contains(search).or(productEntity.productName.contains(search)));
    }
}
