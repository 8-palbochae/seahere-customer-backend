package com.seahere.backend.redis.service;

import com.seahere.backend.incoming.controller.request.IncomingDataRequest;
import com.seahere.backend.incoming.service.IncomingService;
import com.seahere.backend.redis.respository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomingLockFacadeService {

    private static final String TYPE = "incoming";

    private final RedisLockRepository redisLockRepository;
    private final IncomingService incomingService;

    public void save(Long companyId, Long userId, IncomingDataRequest incomingDataRequest) throws InterruptedException {
        while(!redisLockRepository.lock(companyId,TYPE)){
            Thread.sleep(100);
        }
        try{
            incomingService.save(companyId,userId,incomingDataRequest);
        }finally {
            redisLockRepository.unLock(companyId,TYPE);
        }

    }

}
