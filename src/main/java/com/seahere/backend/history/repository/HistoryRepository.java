package com.seahere.backend.history.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seahere.backend.history.dto.HistoryListDto;
import com.seahere.backend.outgoing.entity.OutgoingState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

import static com.seahere.backend.adjust.entity.QAdjustEntity.adjustEntity;
import static com.seahere.backend.company.entity.QCompanyEntity.companyEntity;
import static com.seahere.backend.incoming.entity.QIncomingEntity.incomingEntity;
import static com.seahere.backend.inventory.entity.QInventoryEntity.inventoryEntity;
import static com.seahere.backend.outgoing.entity.QOutgoingEntity.outgoingEntity;

@RequiredArgsConstructor
@Repository
@Slf4j
public class HistoryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<HistoryListDto> findByHistoryDate(Long companyId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Map<LocalDate, HistoryListDto> map = new HashMap<>();
        List<HistoryListDto> incomingResults = queryFactory
                .select(Projections.bean(HistoryListDto.class,
                        incomingEntity.incomingDate.as("date"),
                        incomingEntity.incomingId.count().as("incomingCount"),
                        Expressions.ZERO.longValue().as("outgoingCount"),
                        Expressions.ZERO.longValue().as("adjustCount")))
                .from(incomingEntity)
                .where(incomingEntity.incomingDate.goe(startDate).and(incomingEntity.incomingDate.loe(endDate).and(incomingEntity.company.id.eq(companyId))))
                .groupBy(incomingEntity.incomingDate)
                .fetch();

        for (HistoryListDto dto : incomingResults) {
            map.put(dto.getDate(), dto);
        }

        List<HistoryListDto> outgoingResults = queryFactory
                .select(Projections.bean(HistoryListDto.class,
                        outgoingEntity.outgoingDate.as("date"),
                        Expressions.ZERO.longValue().as("incomingCount"),
                        outgoingEntity.outgoingId.count().as("outgoingCount"),
                        Expressions.ZERO.longValue().as("adjustCount")))
                .from(outgoingEntity)
                .where(outgoingEntity.company.id.eq(companyId).and(outgoingEntity.outgoingDate.goe(startDate)).and(outgoingEntity.outgoingDate.loe(endDate).and(outgoingEntity.outgoingState.ne(OutgoingState.PENDING))))
                .groupBy(outgoingEntity.outgoingDate)
                .fetch();

        for (HistoryListDto dto : outgoingResults) {
            if (map.containsKey(dto.getDate())) {
                HistoryListDto historyListDto = map.get(dto.getDate());
                historyListDto.setOutgoingCount(dto.getOutgoingCount());
                continue;
            }
            map.put(dto.getDate(), dto);
        }

        List<HistoryListDto> adjustResults = queryFactory
                .select(Projections.bean(HistoryListDto.class,
                        adjustEntity.adjustDate.as("date"),
                        Expressions.ZERO.longValue().as("incomingCount"),
                        Expressions.ZERO.longValue().as("outgoingCount"),
                        adjustEntity.adjustId.count().as("adjustCount")))
                .from(adjustEntity)
                .leftJoin(adjustEntity.inventory, inventoryEntity)
                .leftJoin(inventoryEntity.company, companyEntity)
                .where(adjustEntity.adjustDate.goe(startDate).and(adjustEntity.adjustDate.loe(endDate).and(adjustEntity.inventory.company.id.eq(companyId))))
                .groupBy(adjustEntity.adjustDate)
                .fetch();

        for (HistoryListDto dto : adjustResults) {
            if (map.containsKey(dto.getDate())) {
                HistoryListDto historyListDto = map.get(dto.getDate());
                historyListDto.setAdjustCount(dto.getAdjustCount());
                continue;
            }
            map.put(dto.getDate(), dto);
        }
        ArrayList<HistoryListDto> results = new ArrayList<>(map.values());
        results.sort(Comparator.comparing(HistoryListDto::getDate));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());

        if(results.isEmpty()){
            return new SliceImpl<>(results, pageable, false);
        }

        List<HistoryListDto> output = results.subList(start, end);
        boolean hasNext = end < results.size();
        return new SliceImpl<>(output, pageable, hasNext);
    }
}
