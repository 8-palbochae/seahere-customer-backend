package com.seahere.backend.inventory.service;

import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.exception.CompanyNotFound;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.incoming.controller.request.IncomingDataRequest;
import com.seahere.backend.inventory.controller.request.CustomerInventorySearch;
import com.seahere.backend.inventory.controller.request.InventoryRequest;
import com.seahere.backend.inventory.controller.response.CustomerInventoryRes;
import com.seahere.backend.inventory.controller.response.InventoryDetailResponse;
import com.seahere.backend.inventory.controller.response.InventoryResponse;
import com.seahere.backend.inventory.controller.response.InventoryTradeRes;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.exception.InventoryNotFoundException;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import com.seahere.backend.inventory.repository.InventoryRepository;
import com.seahere.backend.product.dto.ProductDto;
import com.seahere.backend.product.entity.ProductEntity;
import com.seahere.backend.product.exception.ProductNotFoundException;
import com.seahere.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryJpaRepository inventoryJpaRepository;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    public Slice<InventoryResponse> findPagedInventoryByCompanyId(Long companyId, Pageable pageable, String search) {
        return inventoryRepository.findPagedInventoryByCompanyId(companyId, search, pageable);
    }

    public Slice<InventoryDetailResponse> findPagedProductsByCompanyId(Long companyId, String name, String category, PageRequest pageable) {
        return inventoryRepository.findPagedProductsByCompanyId(companyId, name, category, pageable);
    }

    private boolean isInventory(Long companyId, IncomingDataRequest incomingDataRequest) {
        ProductEntity productEntity = productRepository.findById(incomingDataRequest.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return inventoryJpaRepository.findByCategoryAndProductNameAndCompanyIdAndNaturalStatusAndCountry(
                incomingDataRequest.getCategory(),
                productEntity.getProductName(),
                companyId,
                incomingDataRequest.getNatural(),
                incomingDataRequest.getCountry()
        ).isPresent();
    }

    public List<CustomerInventoryRes> getBrokerInventoryList(Long companyId, CustomerInventorySearch customerInventorySearch){
        return inventoryRepository.findByCompanyIdWithDetail(companyId, customerInventorySearch)
                .stream()
                .map(CustomerInventoryRes::from)
                .collect(Collectors.toList());
    }


    public List<InventoryTradeRes> findTradeList(Long companyId, CustomerInventorySearch customerInventorySearch){
        return inventoryRepository.findTradeInventory(companyId,customerInventorySearch)
                .stream()
                .map(InventoryTradeRes::from)
                .collect(Collectors.toList());
    }

    public List<InventoryEntity> getBrokerInventoryList(Long companyId){
        return inventoryRepository.findByCompanyIdWithDetail(companyId);

    }

    public InventoryEntity inventoryUpdateEnroll(Long companyId, IncomingDataRequest incomingDataRequest) {
        ProductEntity productEntity = productRepository.findById(incomingDataRequest.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        if (isInventory(companyId, incomingDataRequest)) {
            InventoryEntity inventoryEntity = inventoryJpaRepository.findByCategoryAndProductNameAndCompanyIdAndNaturalStatusAndCountry(
                    incomingDataRequest.getCategory(),
                    productEntity.getProductName(),
                    companyId,
                    incomingDataRequest.getNatural(),
                    incomingDataRequest.getCountry()
            ).orElseThrow(InventoryNotFoundException::new);
            inventoryEntity.addQuantity(incomingDataRequest.getQuantity());
            return inventoryEntity;
        } else {
            InventoryRequest inventoryRequest = InventoryRequest.builder()
                    .companyId(companyId)
                    .quantity(incomingDataRequest.getQuantity())
                    .category(incomingDataRequest.getCategory())
                    .name(productEntity.getProductName())
                    .country(incomingDataRequest.getCountry())
                    .incomingDate(LocalDate.now())
                    .naturalStatus(incomingDataRequest.getNatural())
                    .build();

            InventoryEntity inventoryEntity = inventoryRequest.toEntity();
            inventoryEntity.registProduct(productEntity);
            CompanyEntity company = companyRepository.findById(companyId)
                    .orElseThrow(CompanyNotFound::new);
            inventoryEntity.assignCompany(company);
            inventoryJpaRepository.save(inventoryEntity);
            return inventoryEntity;
        }
    }

    public List<ProductDto> getAllDistinctProductNamesByCompanyId(Long companyId) {
        return inventoryRepository.findAllDistinctProductNamesByCompanyId(companyId);
    }
}
