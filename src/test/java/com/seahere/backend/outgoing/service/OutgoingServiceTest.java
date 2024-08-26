package com.seahere.backend.outgoing.service;

import com.seahere.backend.common.entity.Role;
import com.seahere.backend.common.entity.SocialType;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import com.seahere.backend.inventory.repository.InventoryRepository;
import com.seahere.backend.outgoing.controller.request.OutgoingCreateDetailReq;
import com.seahere.backend.outgoing.controller.request.OutgoingCreateReq;
import com.seahere.backend.outgoing.controller.request.OutgoingSearchReq;
import com.seahere.backend.outgoing.controller.response.OutgoingRes;
import com.seahere.backend.outgoing.controller.response.OutgoingTodayRes;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.OutgoingState;
import com.seahere.backend.outgoing.exception.LackInventoryException;
import com.seahere.backend.outgoing.exception.OutgoingNotFoundException;
import com.seahere.backend.outgoing.repository.OutgoingJpaRepository;
import com.seahere.backend.outgoing.repository.OutgoingRepository;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
@Sql(value = "/sql/outgoing-service-test.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OutgoingServiceTest {
    @Autowired
    private OutgoingService outgoingService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OutgoingJpaRepository outgoingJpaRepository;
    @Autowired
    private OutgoingRepository outgoingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Test
    @DisplayName("출고 리스트중 상태가 해당 회사 번호가 없다면 0개를 던진다.")
    void findByOutgoingStateIsPendingIsEmpty() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "outgoingId"));
        //when
        //then

        Slice<OutgoingEntity> result = outgoingService.findByOutgoingStateIsPending(3L, pageRequest, LocalDate.of(2024, 7, 20), LocalDate.of(2024, 7, 30), "");
        assertThat(result.getContent()).hasSize(0);

    }

    @Test
    @DisplayName("출고 리스트중 상태가 해당 회사 번호와 출고요청(pending), 지정날짜 사이의 출고 목록을 반환한다.")
    void findByOutgoingStateIsPendingSlice(){
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "outgoingId"));
        CompanyEntity company = companyRepository.findById(101L).get();
        //when
        Slice<OutgoingEntity> result = outgoingService.findByOutgoingStateIsPending(101L, pageRequest,LocalDate.of(2024,7,20),LocalDate.of(2024,7,30),"");

        //then
        assertThat(result.getContent()).hasSize(3)
                .extracting("company","outgoingState","partialOutgoing")
                .contains(
                        tuple(company,OutgoingState.PENDING,true),
                        tuple(company,OutgoingState.PENDING,true),
                        tuple(company,OutgoingState.PENDING,true)
                );
    }
    @Test
    @DisplayName("출고 리스트중 상태가 해당 회사 번호와 출고요청(pending), 지정날짜 사이 그리고 이름이 아리가 포함된 출고 목록을 반환한다.")
    void findByOutgoingStateIsPendingSliceSearch(){
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "outgoingId"));
        CompanyEntity company = companyRepository.findById(101L).get();
        UserEntity 아리랑 = userRepository.findById(101L).get();
        //when
        Slice<OutgoingEntity> result = outgoingService.findByOutgoingStateIsPending(101L, pageRequest,LocalDate.of(2024,7,20),LocalDate.of(2024,8,20),"아리랑");

        //then
        assertThat(result.getContent()).hasSize(1)
                .extracting("company","outgoingState","partialOutgoing","customer")
                .contains(
                        tuple(company,OutgoingState.PENDING,true,아리랑)
                );
    }
    @Test
    @DisplayName("출고 리스트중 상태가 해당 회사 번호와 출고요청(pending), 지정날짜 사이 그리고 상품이름이 광어가 포함된 출고 목록을 반환한다.")
    void findByOutgoingStateIsPendingSliceSearchProductName(){
        //given
        CompanyEntity company = companyRepository.findById(101L).get();
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "outgoingId"));
        UserEntity 아리랑 = userRepository.findById(101L).get();
        UserEntity 스리랑 = userRepository.findById(201L).get();
        //when
        Slice<OutgoingEntity> result = outgoingService.findByOutgoingStateIsPending(101L, pageRequest,LocalDate.of(2024,7,20),LocalDate.of(2024,7,30),"광어");
        //then
        assertThat(result.getContent().get(0).getOutgoingDetails().get(0).getProduct().getProductName()).isEqualTo("광어");
        assertThat(result.getContent()).hasSize(2)
                .extracting("company","outgoingState","partialOutgoing","customer")
                .contains(
                        tuple(company,OutgoingState.PENDING,true,아리랑),
                        tuple(company,OutgoingState.PENDING,true,스리랑)
                );
    }

    @Test
    @DisplayName("출고대기(ready)로 출고의 상태를 변경한다")
    void changeOutgoingStateToReady() {
        // given
        OutgoingEntity outgoing = outgoingJpaRepository.findById(101L).get();
        // when
        outgoing.changeState(OutgoingState.READY);
        OutgoingEntity result = outgoingJpaRepository.findById(101L).get();
        // then
        assertThat(result.getOutgoingState()).isEqualTo(OutgoingState.READY);
    }

    @Test
    @DisplayName("출고완료(complete)로 출고의 상태를 변경한다")
    void changeOutgoingStateToComplete() {
        // given
        OutgoingEntity outgoing = outgoingJpaRepository.findById(101L).get();
        // when
        outgoing.changeState(OutgoingState.COMPLETE);
        OutgoingEntity result = outgoingJpaRepository.findById(101L).get();
        // then
        assertThat(result.getOutgoingState()).isEqualTo(OutgoingState.COMPLETE);
    }

//    @Test
//    @DisplayName("해당하는 인벤토리의 재고를 감소시키로 출고를 출고 대기 상태로 변경")
//    void acceptOutgoingCall() {
//        // given
//        var outgoing = outgoingJpaRepository.findById(101L).get();
//        var inventory = inventoryJpaRepository.findById(101L).get();
//        // when
//        outgoingService.changeOutgoingState(101L,OutgoingState.READY);
//        // then
//        assertThat(outgoing.getOutgoingState()).isEqualTo(OutgoingState.READY);
//        assertThat(inventory.getQuantity()).isEqualTo(80);
//    }
//    @Test
//    @DisplayName("인벤토리의 재고가 부족할시 재고 부족 예외를 던진다.")
//    void acceptOutgoingCalllack() {
//        // given
//        OutgoingEntity outgoing = outgoingJpaRepository.findById(301L).get();
//        // when
//        // then
//        assertThatThrownBy(() -> outgoingService.changeOutgoingState(301L,OutgoingState.READY)).isInstanceOf(LackInventoryException.class)
//                .hasMessage("보유 재고가 요청 재고보다 부족합니다.");
//        assertThat(outgoing.getOutgoingState()).isEqualTo(OutgoingState.PENDING);
//    }

    @Test
    @DisplayName("출고 요청 재고 목록을 담은 요청 DTO를 통해서 출고 요청을 생성할 수 있다.")
    void createOutgoing() throws Exception {
        //given
        Long userId = 101L;

        OutgoingCreateDetailReq detailOne = OutgoingCreateDetailReq.builder()
                .inventoryId(101L)
                .price(BigDecimal.valueOf(10000L))
                .quantity(3F)
                .build();

        OutgoingCreateDetailReq detailTwo = OutgoingCreateDetailReq.builder()
                .inventoryId(201L)
                .price(BigDecimal.valueOf(20000L))
                .quantity(1F)
                .build();
        List<OutgoingCreateDetailReq> outgoingDetails = List.of(detailOne,detailTwo);

        OutgoingCreateReq request = OutgoingCreateReq.builder()
                .details(outgoingDetails)
                .companyId(101L)
                .partialOutgoing(true)
                .build();

        //when
        Long saveId = outgoingService.save(request, userId);

        OutgoingEntity result = outgoingJpaRepository.findById(saveId)
                .orElseThrow(OutgoingNotFoundException::new);

        //then
        assertTrue(result.isPartialOutgoing());
        assertEquals(OutgoingState.PENDING,result.getOutgoingState());
        assertEquals(2L,result.getOutgoingDetails().size());
        assertEquals(BigDecimal.valueOf(10000L),result.getOutgoingDetails().get(0).getPrice());
        assertEquals(3F,result.getOutgoingDetails().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(20000L),result.getOutgoingDetails().get(1).getPrice());
        assertEquals(1F,result.getOutgoingDetails().get(1).getQuantity());
    }


}