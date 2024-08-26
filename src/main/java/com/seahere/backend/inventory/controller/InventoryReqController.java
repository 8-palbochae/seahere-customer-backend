package com.seahere.backend.inventory.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.inventory.controller.request.CustomerInventorySearch;
import com.seahere.backend.inventory.controller.request.InventoryReqDetailSearchRequest;
import com.seahere.backend.inventory.controller.request.InventoryReqSearchRequest;
import com.seahere.backend.inventory.controller.response.*;
import com.seahere.backend.inventory.service.InventoryService;
import com.seahere.backend.product.controller.response.IncomingSearchResponse;
import com.seahere.backend.product.dto.ProductDto;
import com.seahere.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class InventoryReqController {
    private final InventoryService inventoryService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<InventoryReqListResponse> inventoryReqList(InventoryReqSearchRequest searchRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        PageRequest pageRequest = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by(Sort.Direction.ASC, "name"));
        Slice<InventoryResponse> results = inventoryService.findPagedInventoryByCompanyId(customUserDetails.getUser().getCompanyId(), pageRequest, searchRequest.getSearch());
        InventoryReqListResponse inventoryReqListResponse = new InventoryReqListResponse(results);
        return ResponseEntity.ok(inventoryReqListResponse);
    }

    @GetMapping("/details")
    public ResponseEntity<InventoryReqDetailListResponse> inventoryReqDetailList(InventoryReqDetailSearchRequest detailSearchRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        PageRequest pageRequest = PageRequest.of(detailSearchRequest.getPage(), detailSearchRequest.getSize(), Sort.by(Sort.Direction.ASC, "incomingDate"));
        Slice<InventoryDetailResponse> results = inventoryService.findPagedProductsByCompanyId(customUserDetails.getUser().getCompanyId(), detailSearchRequest.getName(), detailSearchRequest.getCategory(), pageRequest);
        InventoryReqDetailListResponse inventoryReqDetailListResponse = new InventoryReqDetailListResponse(results);
        return ResponseEntity.ok(inventoryReqDetailListResponse);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<List<CustomerInventoryRes>> customerInventoryListGet(@PathVariable Long companyId,
                                                                               @ModelAttribute CustomerInventorySearch customerInventorySearch) {
        List<CustomerInventoryRes> brokerInventoryList = inventoryService.getBrokerInventoryList(companyId, customerInventorySearch);
        return ResponseEntity.ok(brokerInventoryList);
    }

    @GetMapping("/trades/{companyId}")
    public ResponseEntity<List<InventoryTradeRes>> findInventoryTrades(@PathVariable Long companyId, @ModelAttribute CustomerInventorySearch customerInventorySearch){
        List<InventoryTradeRes> brokerTradeList = inventoryService.findTradeList(companyId, customerInventorySearch);
        return ResponseEntity.ok(brokerTradeList);
    }

    @GetMapping("/product-search-inventories")
    public ResponseEntity<List<IncomingSearchResponse>> getInventory(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ProductDto> products = inventoryService.getAllDistinctProductNamesByCompanyId(customUserDetails.getUser().getCompanyId());
        List<IncomingSearchResponse> productResponses = products.stream()
                .map(IncomingSearchResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productResponses);
    }
}
