package com.seahere.backend.incoming.service;

import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.exception.CompanyNotFound;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.incoming.controller.request.IncomingDataRequest;
import com.seahere.backend.incoming.controller.request.IncomingEditReq;
import com.seahere.backend.incoming.entity.IncomingEntity;
import com.seahere.backend.incoming.exception.IncomingNotFound;
import com.seahere.backend.incoming.repository.IncomingJpaRepository;
import com.seahere.backend.inventory.service.InventoryService;
import com.seahere.backend.product.entity.ProductEntity;
import com.seahere.backend.product.repository.ProductRepository;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.exception.UserNotFound;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class IncomingServiceImpl implements IncomingService{

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final IncomingJpaRepository incomingJpaRepository;
    private final InventoryService inventoryService;

    @Override
    public void save(Long companyId, Long userId, IncomingDataRequest incomingDataRequest) {
        ProductEntity productEntity = productRepository.findById(incomingDataRequest.getProductId()).orElse(null);
        CompanyEntity companyEntity = companyRepository.findById(companyId).orElseThrow(CompanyNotFound::new);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        IncomingEntity incomingEntity = incomingDataRequest.toEntity(productEntity);
        incomingEntity.enroll(userEntity,companyEntity);
        inventoryService.inventoryUpdateEnroll(companyId, incomingDataRequest);
        incomingJpaRepository.save(incomingEntity);
    }

    @Override
    public Long editIncoming(IncomingEditReq incomingEditReq) {
        IncomingEntity incoming = incomingJpaRepository.findById(incomingEditReq.getIncomingId())
                .orElseThrow(IncomingNotFound::new);
        incoming.edit(incomingEditReq.getPrice());
        incomingJpaRepository.save(incoming);
        return incoming.getIncomingId();
    }
}