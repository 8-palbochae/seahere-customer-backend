package com.seahere.backend.inventory.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.inventory.controller.request.InventoryEditReq;
import com.seahere.backend.inventory.controller.response.InventorySettingRes;
import com.seahere.backend.inventory.service.InventoryDetailService;
import com.seahere.backend.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class InventoryDetailController {
    private final InventoryDetailService inventoryDetailService;
    private final InventoryService inventoryService;

    @PostMapping("inventories/detail/{inventoryId}")
    public ResponseEntity<InventorySettingRes> inventoryDetailPost(@PathVariable Long inventoryId, @RequestBody InventoryEditReq inventoryEditReq){
        InventorySettingRes inventorySettingRes = inventoryDetailService.editInventoryDetail(inventoryId, inventoryEditReq);
        return ResponseEntity.ok(inventorySettingRes);
    }

    @GetMapping("/inventories/detail")
    public ResponseEntity<List<InventorySettingRes>> inventoryDetailList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<InventorySettingRes> result = inventoryService.getBrokerInventoryList(customUserDetails.getUser().getCompanyId()).stream().map(InventorySettingRes::new).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
