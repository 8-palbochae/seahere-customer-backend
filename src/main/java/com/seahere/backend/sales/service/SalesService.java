package com.seahere.backend.sales.service;

import com.seahere.backend.sales.dto.FishDto;
import com.seahere.backend.sales.dto.SalesMonthDto;
import com.seahere.backend.sales.dto.SalesWeekDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesService {

    List<SalesWeekDto> findIncomingWeek(LocalDate startDate, LocalDate endDate, Long companyId);
    List<SalesMonthDto> findIncomingMonth(LocalDate startDate, LocalDate endDate, Long companyId);
    List<SalesWeekDto> findOutgoingWeek(LocalDate startDate, LocalDate endDate, Long companyId);
    List<SalesMonthDto> findOutgoingMonth(LocalDate startDate, LocalDate endDate, Long companyId);
    List<FishDto> findFish(LocalDate startDate, LocalDate endDate, Long companyId);
}
