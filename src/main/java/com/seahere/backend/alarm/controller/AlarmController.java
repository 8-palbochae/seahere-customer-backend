package com.seahere.backend.alarm.controller;

import com.seahere.backend.alarm.controller.request.DiscountRequest;
import com.seahere.backend.alarm.controller.request.TokenRequest;
import com.seahere.backend.alarm.controller.response.AlarmHistoryResponse;
import com.seahere.backend.alarm.controller.response.InventoryRes;
import com.seahere.backend.alarm.entity.AlarmHistoryEntity;
import com.seahere.backend.alarm.service.AlarmService;
import com.seahere.backend.alarm.service.DiscountService;
import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final InventoryService inventoryService;
    private final DiscountService discountService;

    @PostMapping("/firebase-token")
    public void tokenSave(@RequestBody TokenRequest tokenRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        log.info("TokenRequest = {}, userId = {}", tokenRequest.getToken(),userDetails.getUser().getUserId());
        alarmService.saveToken(userDetails.getUser().getUserId(),tokenRequest.getToken());
    }

    @GetMapping("/alarm/inventories")
    public ResponseEntity<List<InventoryRes>> inventoryListGet(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<InventoryRes> results = inventoryService.getBrokerInventoryList(userDetails.getUser().getCompanyId()).stream()
                .map(InventoryRes::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @PostMapping("/alarm/discounts")
    public void alarmDiscount(@RequestBody DiscountRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){
        log.info("request = {}", request);
        discountService.discountInventory(userDetails.getUser().getCompanyId(),request);
    }

    @PostMapping("/alarm/log")
    public void alarmLog(@RequestParam("fish")String fish, @AuthenticationPrincipal CustomUserDetails userDetails){
        log.info("fish = {}", fish);
        alarmService.saveAlarmClickLog(fish, userDetails.getUser().getUserId());
    }

    @GetMapping("/alarm/histories")
    public ResponseEntity<AlarmHistoryResponse> alarmHistories(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("page")int page,
                                                               @RequestParam(name = "size", defaultValue = "10")int size){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Slice<AlarmHistoryEntity> histories = alarmService.findByUserId(userDetails.getUser().getUserId(), pageRequest);
        return ResponseEntity.ok(new AlarmHistoryResponse(histories));
    }
}
