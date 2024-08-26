package com.seahere.backend.sales.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.sales.controller.request.PeriodRequest;
import com.seahere.backend.sales.controller.response.FishDataRes;
import com.seahere.backend.sales.controller.response.SalesMonthRes;
import com.seahere.backend.sales.controller.response.SalesWeekRes;
import com.seahere.backend.sales.dto.FishDto;
import com.seahere.backend.sales.dto.SalesMonthDto;
import com.seahere.backend.sales.dto.SalesWeekDto;
import com.seahere.backend.sales.service.SalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @PostMapping("/incoming/week")
    public ResponseEntity<List<SalesWeekRes>> findIncomingWeek(@RequestBody PeriodRequest incomingWeekRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SalesWeekDto> result = salesService.findIncomingWeek(
                incomingWeekRequest.getStartDate(),
                incomingWeekRequest.getEndDate(),
                userDetails.getUser().getCompanyId()
        );

        List<SalesWeekRes> responseList = result.stream()
                .map(dto -> SalesWeekRes.builder()
                        .commonDate(dto.getCommonDate())
                        .week(dto.getWeek())
                        .commonPrice(dto.getCommonPrice())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/incoming/month")
    public ResponseEntity<List<SalesMonthRes>> findIncomingMonth(@RequestBody PeriodRequest incomingMonthRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
    List<SalesMonthDto> result = salesService.findIncomingMonth(incomingMonthRequest.getStartDate(),
            incomingMonthRequest.getEndDate(),
            userDetails.getUser().getCompanyId());

    List<SalesMonthRes> responseList = result.stream()
            .map(dto-> SalesMonthRes.builder()
                    .month(dto.getMonth())
                    .commonPrice(dto.getCommonPrice())
                    .build()).collect(Collectors.toList());
    return ResponseEntity.ok(responseList);
    }

    @PostMapping("/outgoing/week")
    public ResponseEntity<List<SalesWeekRes>> findOutgoingWeek(@RequestBody PeriodRequest outgoingWeekRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<SalesWeekDto> result = salesService.findOutgoingWeek(
                outgoingWeekRequest.getStartDate(),
                outgoingWeekRequest.getEndDate(),
                userDetails.getUser().getCompanyId()
        );

        List<SalesWeekRes> responseList = result.stream()
                .map(dto -> SalesWeekRes.builder()
                        .commonDate(dto.getCommonDate())
                        .week(dto.getWeek())
                        .commonPrice(dto.getCommonPrice())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);

    }
    @PostMapping("/outgoing/month")
    public ResponseEntity<List<SalesMonthRes>> findOutgoingMonth(@RequestBody PeriodRequest outgoingMonthRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SalesMonthDto> result = salesService.findOutgoingMonth(outgoingMonthRequest.getStartDate(),
                outgoingMonthRequest.getEndDate(),
                userDetails.getUser().getCompanyId()
        );

        List<SalesMonthRes> responseList = result.stream()
                .map(dto-> SalesMonthRes.builder()
                        .month(dto.getMonth())
                        .commonPrice(dto.getCommonPrice())
                        .build()).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }
    @PostMapping("/fish/chart")
    public ResponseEntity<List<FishDataRes>> findFishData(@RequestBody PeriodRequest periodRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<FishDto> result = salesService.findFish(periodRequest.getStartDate()
                ,periodRequest.getEndDate()
                ,userDetails.getUser().getCompanyId());
        List<FishDataRes> responseList = result.stream()
                .map(dto->FishDataRes.builder()
                        .productName(dto.getProductName())
                        .productImg(dto.getProductImg())
                        .price((dto.getPrice()))
                        .build()).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }




}
