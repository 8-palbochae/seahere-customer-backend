package com.seahere.backend.outgoing.service;

import com.seahere.backend.outgoing.controller.response.CustomerOutgoingDetailRes;
import com.seahere.backend.outgoing.dto.OutgoingDetailDto;
import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingDetailState;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.exception.NotPartialOutgoingException;
import com.seahere.backend.outgoing.exception.OutgoingDetailNotFoundException;
import com.seahere.backend.outgoing.exception.OutgoingNotFoundException;
import com.seahere.backend.outgoing.repository.OutgoingDetailJpaRepository;
import com.seahere.backend.outgoing.repository.OutgoingDetailRepository;
import com.seahere.backend.outgoing.repository.OutgoingJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OutgoingDetailService {

    private final OutgoingDetailJpaRepository outgoingDetailJpaRepository;
    private final OutgoingJpaRepository outgoingJpaRepository;
    private final OutgoingDetailRepository outgoingDetailRepository;

    @Transactional
    public void deleteOutgoingDetail(Long outgoingDetailId){
        OutgoingDetailEntity detail = outgoingDetailJpaRepository.findById(outgoingDetailId).orElseThrow(OutgoingDetailNotFoundException::new);
        if(detail.isPossibleDelete()){
            detail.stateToDelete();
            return;
        }
        throw new NotPartialOutgoingException();
    }

    public List<OutgoingDetailDto> findByOutgoingAndStateIsAcitve(Long outgoingId){
        return outgoingDetailRepository.findByOutgoingAndStateActive(outgoingId);
    }

    @Transactional
    public void updateByOutgoingDetailStateToActive(Long outgoingId){
        OutgoingEntity outgoing = outgoingJpaRepository.findById(outgoingId).orElseThrow(OutgoingNotFoundException::new);
        outgoingDetailJpaRepository.updateByOutgoingDetailStateToActive(outgoing,OutgoingDetailState.ACTIVE);
    }

    public List<CustomerOutgoingDetailRes> getCustomerOutgoingList(Long outgoingId){
        return outgoingDetailRepository.findByOutgoingId(outgoingId)
                .stream()
                .map(CustomerOutgoingDetailRes::from)
                .collect(Collectors.toList());
    }
}
