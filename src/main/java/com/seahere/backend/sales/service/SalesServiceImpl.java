package com.seahere.backend.sales.service;

import com.seahere.backend.sales.dto.FishDto;
import com.seahere.backend.sales.dto.SalesMonthDto;
import com.seahere.backend.sales.dto.SalesWeekDto;
import com.seahere.backend.sales.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;

    @Override
    public List<SalesWeekDto> findIncomingWeek(LocalDate startDate, LocalDate endDate, Long companyId) {
        return salesRepository.incomingWeekList(companyId, startDate, endDate);
    }

    @Override
    public List<SalesMonthDto> findIncomingMonth(LocalDate startDate, LocalDate endDate, Long companyId) {
        return salesRepository.incomingMonthList(companyId, startDate, endDate);
    }

    @Override
    public List<SalesWeekDto> findOutgoingWeek(LocalDate startDate, LocalDate endDate, Long companyId) {
        return salesRepository.outgoingWeekList(companyId, startDate, endDate);
    }

    @Override
    public List<SalesMonthDto> findOutgoingMonth(LocalDate startDate, LocalDate endDate, Long companyId) {
        return salesRepository.outgoingMonthList(companyId, startDate, endDate);
    }

    @Override
    public List<FishDto> findFish(LocalDate startDate, LocalDate endDate, Long companyId) {
        return salesRepository.fishList(companyId, startDate, endDate);
    }
}
