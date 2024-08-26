package com.seahere.backend.redis.service;

import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.OutgoingState;
import com.seahere.backend.outgoing.service.OutgoingService;
import com.seahere.backend.redis.respository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OutgoingLockFacadeService {

    private final static String TYPE = "outgoing";

    private final RedisLockRepository redisLockRepository;
    private final OutgoingService outgoingService;

    public OutgoingEntity changeOutgoingState(Long companyId,Long outgoingId, OutgoingState state) throws InterruptedException {
        while(!redisLockRepository.lock(companyId,TYPE)){
            Thread.sleep(100);
        }
        try{
            return outgoingService.changeOutgoingState(outgoingId, state);
        }finally {
            redisLockRepository.unLock(companyId,TYPE);
        }
    }
}
