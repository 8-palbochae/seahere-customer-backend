package com.seahere.backend.outgoing.service;

import com.seahere.backend.alarm.dto.AlarmToCompanyEvent;
import com.seahere.backend.alarm.dto.AlarmToCustomerEvent;
import com.seahere.backend.common.dto.UserLogin;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.exception.CompanyNotFound;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.exception.InventoryNotFoundException;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import com.seahere.backend.inventory.repository.InventoryRepository;
import com.seahere.backend.outgoing.controller.request.OutgoingCreateDetailReq;
import com.seahere.backend.outgoing.controller.request.OutgoingCreateReq;
import com.seahere.backend.outgoing.controller.request.OutgoingSearchReq;
import com.seahere.backend.outgoing.controller.response.OutgoingRes;
import com.seahere.backend.outgoing.controller.response.OutgoingTodayRes;
import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingDetailState;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.OutgoingState;
import com.seahere.backend.outgoing.exception.LackInventoryException;
import com.seahere.backend.outgoing.exception.OutgoingNotFoundException;
import com.seahere.backend.outgoing.repository.OutgoingJpaRepository;
import com.seahere.backend.outgoing.repository.OutgoingRepository;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.exception.UserNotFound;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OutgoingService {

    private final OutgoingJpaRepository outgoingJpaRepository;
    private final InventoryJpaRepository inventoryJpaRepository;
    private final InventoryRepository inventoryRepository;
    private final OutgoingRepository outgoingRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public Slice<OutgoingEntity> findByOutgoingStateIsPending(Long companyId, Pageable pageable, LocalDate startDate, LocalDate endDate, String search){
        return outgoingRepository.findByOutgoingStateIsPending(companyId,pageable, startDate, endDate, search);
    }
    @Transactional
    public Long save(OutgoingCreateReq outgoingCreateReq, Long userId){
        UserEntity customer = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        CompanyEntity company = companyRepository.findById(outgoingCreateReq.getCompanyId())
                .orElseThrow(CompanyNotFound::new);

        OutgoingEntity outgoing = OutgoingEntity.builder()
                .company(company)
                .partialOutgoing(outgoingCreateReq.isPartialOutgoing())
                .tradeType(outgoingCreateReq.getTradeType())
                .outgoingState(OutgoingState.PENDING)
                .customer(customer)
                .outgoingDate(LocalDate.now())
                .build();

        outgoingCreateReq.getDetails()
                .forEach(detail -> {
                    OutgoingDetailEntity outgoingDetail = createOutgoingDetail(detail);
                    outgoing.addOutgoingDetail(outgoingDetail);
                });

        outgoingJpaRepository.save(outgoing);
        eventPublisher.publishEvent(new AlarmToCompanyEvent(company.getId(), "출고 요청","새로운 출고 요청이 있습니다."));
        return outgoing.getOutgoingId();
    }

    @Transactional
    public OutgoingEntity changeOutgoingState(Long outgoingId, OutgoingState state){

        if(OutgoingState.READY.equals(state)){
            OutgoingEntity outgoingCall = acceptOutgoingCall(outgoingId);
            outgoingCall.changeState(state);
            eventPublisher.publishEvent(new AlarmToCustomerEvent(outgoingCall.getCustomer().getId(),"출고 상태 변경","주문이 "+ state.printState() +"상태로 변경되었습니다."));
            return outgoingCall;
        }

        OutgoingEntity outgoingCall = outgoingJpaRepository.findById(outgoingId).orElseThrow(OutgoingNotFoundException::new);
        outgoingCall.changeState(state);
        eventPublisher.publishEvent(new AlarmToCustomerEvent(outgoingCall.getCustomer().getId(),"출고 상태 변경","주문이 "+ state.printState() +"상태로 변경되었습니다."));
        return outgoingCall;
    }

    public List<OutgoingRes> getList(OutgoingSearchReq outgoingSearchReq, Long userId){
         return outgoingRepository.findByOutgoingByCustomerId(outgoingSearchReq, userId)
                .stream().map(OutgoingRes::from)
                .collect(Collectors.toList());
    }

    public OutgoingTodayRes getTodayInfo(Long customerId){
        LocalDate now = LocalDate.now();
        List<OutgoingEntity> customerTodayList = outgoingJpaRepository.findByCustomerIdAndOutgoingDate(customerId, now);

        Map<OutgoingState, Long> statusCounts = customerTodayList.stream()
                .collect(Collectors.groupingBy(OutgoingEntity::getOutgoingState, Collectors.counting()));

        return OutgoingTodayRes.builder()
                .pending(statusCounts.getOrDefault(OutgoingState.PENDING,0L))
                .ready(statusCounts.getOrDefault(OutgoingState.READY,0L))
                .complete(statusCounts.getOrDefault(OutgoingState.COMPLETE,0L))
                .build();
    }

    public OutgoingRes getRecentlyOutgoing(Long customerId){
        OutgoingEntity recentlyOutgoing = outgoingRepository.findByCustomerRecently(customerId);

        if(recentlyOutgoing == null){
            return OutgoingRes.builder().build();
        }

        return OutgoingRes.from(recentlyOutgoing);
    }

    private OutgoingEntity acceptOutgoingCall(Long outgoingId){
        OutgoingEntity outgoingCall = outgoingJpaRepository.findByIdFetchCompany(outgoingId).orElseThrow(OutgoingNotFoundException::new);
        CompanyEntity company = outgoingCall.getCompany();
        List<OutgoingDetailEntity> details = outgoingCall.getOutgoingDetails().stream().filter(OutgoingDetailEntity::isNotDelete).collect(Collectors.toList());
        for(OutgoingDetailEntity detail : details){
             InventoryEntity inventory= inventoryJpaRepository.findByCategoryAndProductNameAndCompanyIdAndNaturalStatusAndCountry(detail.getCategory(), detail.getProduct().getProductName(), company.getId(), detail.getNaturalStatus(), detail.getCountry())
                    .orElseThrow(InventoryNotFoundException::new);
             if(detail.isLackInventory(inventory.getQuantity())) throw new LackInventoryException();
             inventory.minusQuantity(detail.getQuantity());

             if(inventory.getQuantity() <= inventory.getInventoryDetail().getWarningQuantity()){
                 eventPublisher.publishEvent(new AlarmToCompanyEvent(company.getId(),"안전 재고 부족",inventory.getProduct().getProductName() + "가 안전재고보다 수량이 부족합니다."));
             }
        }
        return outgoingCall;
    }

    private OutgoingDetailEntity createOutgoingDetail(OutgoingCreateDetailReq detailReq) {
        InventoryEntity inventory = inventoryRepository.findByIdWithProduct(detailReq.getInventoryId())
                .orElseThrow(InventoryNotFoundException::new);

        return OutgoingDetailEntity.builder()
                .price(detailReq.getPrice())
                .quantity(detailReq.getQuantity())
                .naturalStatus(inventory.getNaturalStatus())
                .country(inventory.getCountry())
                .category(inventory.getCategory())
                .product(inventory.getProduct())
                .state(OutgoingDetailState.ACTIVE)
                .build();
    }
}
