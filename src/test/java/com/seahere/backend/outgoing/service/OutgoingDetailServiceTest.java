package com.seahere.backend.outgoing.service;

import com.seahere.backend.outgoing.dto.OutgoingDetailDto;
import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingDetailState;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.repository.OutgoingDetailJpaRepository;
import com.seahere.backend.outgoing.repository.OutgoingJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
@Sql(value = "/sql/outgoing-detail-service-test.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Slf4j
class OutgoingDetailServiceTest {

    @Autowired
    private OutgoingDetailService outgoingDetailService;

    @Autowired
    private OutgoingDetailJpaRepository outgoingDetailJpaRepository;

    @Autowired
    private EntityManager em;
    @Autowired
    private OutgoingJpaRepository outgoingJpaRepository;

    @Test
    @DisplayName("일치하는 출고 상세 번호에 대항하는 출고 상세의 상태를 delete로 바꾼다.")
    void stateToDelete() {
        //given
        outgoingDetailService.deleteOutgoingDetail(101L);
        //when
        OutgoingDetailEntity result = outgoingDetailJpaRepository.findById(101L).get();
        //then
        assertThat(result.getState()).isEqualTo(OutgoingDetailState.DELETE);
    }

    @Test
    @DisplayName("일치하는 출고 상세 번호에 대항하는 출고 상세의 상태를 state로 바꾼다.")
    void stateToState() {
        //given
        OutgoingDetailEntity detail = outgoingDetailJpaRepository.findById(101L).get();
        //when
        detail.stateToActive();
        em.flush();
        em.clear();
        OutgoingDetailEntity result = outgoingDetailJpaRepository.findById(101L).get();
        //then
        assertThat(result.getState()).isEqualTo(OutgoingDetailState.ACTIVE);
    }

    @Test
    @DisplayName("출고 번호일치하고 상태가 ACTIVE 인 출고 상세 목록을 보여준다.")
    void findByOutgoingAndStateIsAcitve() {
        // given
        // when
        List<OutgoingDetailDto> result = outgoingDetailService.findByOutgoingAndStateIsAcitve(101L);

        // then
        assertThat(result).hasSize(1).extracting("productName","outgoingQuantity","inventoryQuantity")
                .contains(


                        tuple("광어", 20f, 100f)

                );
    }

    @Test
    @DisplayName("선택한 출고번호에 해당하는 출고상세목록의 상태를 ACTIVE로 변경한다.")
    void updateByOutgoingDetailStateToActive(){
        //given
        //when
        outgoingDetailService.updateByOutgoingDetailStateToActive(201L);
        OutgoingEntity outgoing = outgoingJpaRepository.findById(201L).get();
        //then

        assertThat(outgoing.getOutgoingDetails()).isEmpty();
    }

}