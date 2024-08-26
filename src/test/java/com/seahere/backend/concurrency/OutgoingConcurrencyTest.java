package com.seahere.backend.concurrency;

import com.seahere.backend.alarm.exception.TokenNotFoundException;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import com.seahere.backend.outgoing.entity.OutgoingState;
import com.seahere.backend.outgoing.service.OutgoingService;
import com.seahere.backend.redis.service.OutgoingLockFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = "/sql/outgoing-concurrency-test.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Slf4j
public class OutgoingConcurrencyTest {
    @Autowired
    private OutgoingService outgoingService;
    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;
    @Autowired
    private OutgoingLockFacadeService outgoingLockFacadeService;

    @Test
    @DisplayName("출고할때 동시성 실패 테스트")
    void outgoging() throws InterruptedException {
        // given
        // when
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        for(int i = 0; i < threadCount; i++){
            Long id = (long) i;
            executorService.submit(() -> {
                try{
                    outgoingService.changeOutgoingState(id, OutgoingState.READY);
                    successCount.incrementAndGet();
                }catch (TokenNotFoundException ignored){

                }catch (Exception e){
                    failCount.incrementAndGet();
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        // then
        InventoryEntity inventory = inventoryJpaRepository.findById(1L).get();
        log.info("재고 수 = {}",inventory.getQuantity());
        assertThat(inventory.getQuantity()).isNotEqualTo(20);
    }
    @Test
    @DisplayName("출고할때 동시성 성공 테스트")
    void outgoingLock() throws InterruptedException {
        // given
        // when
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        for(int i = 0; i < threadCount; i++){
            Long id = (long) i;
            executorService.submit(() -> {
                try{
                    outgoingLockFacadeService.changeOutgoingState(1L,id, OutgoingState.READY);
                    successCount.incrementAndGet();
                }catch (RuntimeException e){
                    e.printStackTrace();
                }catch (Exception e){
                    failCount.incrementAndGet();
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        // then
        InventoryEntity inventory = inventoryJpaRepository.findById(1L).get();
        log.info("재고 수 = {}",inventory.getQuantity());
        assertThat(inventory.getQuantity()).isEqualTo(20);
    }
}
