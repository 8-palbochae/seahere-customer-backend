package com.seahere.backend.concurrency;

import com.seahere.backend.incoming.controller.request.IncomingDataRequest;
import com.seahere.backend.incoming.entity.IncomingEntity;
import com.seahere.backend.incoming.repository.IncomingJpaRepository;
import com.seahere.backend.incoming.service.IncomingService;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import com.seahere.backend.redis.service.IncomingLockFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
@Sql(value = "/sql/incoming-concurrency-test.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class IncomingConcurrencyTest {

    @Autowired
    private IncomingService incomingService;
    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;
    @Autowired
    private IncomingJpaRepository incomingJpaRepository;
    @Autowired
    private IncomingLockFacadeService incomingLockFacadeService;

    @Test
    @DisplayName("입고할때 동시성 확인 실패 케이스")
    void save() throws InterruptedException {
        // given
        IncomingDataRequest request = IncomingDataRequest.builder().incomingPrice(1000)
                .productId(1L)
                .natural("자연")
                .category("활어")
                .country("국산")
                .quantity(1)
                .build();
        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        // when

        for(int i = 0; i < threadCount; i++){
            executorService.submit(() ->{
                try{
                    incomingService.save(1L,1L,request);
                    successCount.incrementAndGet();
                }catch (RuntimeException e){
                    e.printStackTrace();
                    failCount.incrementAndGet();
                }catch (Exception e){
                    e.printStackTrace();
                    failCount.incrementAndGet();
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        // then
        log.info("성공횟수 = {}, 실패 횟수 = {}",successCount, failCount);
        List<IncomingEntity> list = incomingJpaRepository.findAll();
        assertThat(list.size()).isNotEqualTo(30);
    }


    @Test
    @DisplayName("입고할떄 동시성 확인 성공")
    void saveLock() throws InterruptedException {
        // given
        IncomingDataRequest request = IncomingDataRequest.builder().incomingPrice(1000)
                .productId(1L)
                .natural("자연")
                .category("활어")
                .country("국산")
                .quantity(1)
                .build();
        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        // when
        for(int i = 0; i < threadCount; i++){
            executorService.submit(() ->{
                try{
                    incomingLockFacadeService.save(1L,1L,request);
                    successCount.incrementAndGet();
                }catch (RuntimeException e){
                    e.printStackTrace();
                    failCount.incrementAndGet();
                }catch (Exception e){
                    e.printStackTrace();
                    failCount.incrementAndGet();
                }
                finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        InventoryEntity inventory = inventoryJpaRepository.findByCategoryAndProductNameAndCompanyIdAndNaturalStatusAndCountry("활어", "광어", 1L, "자연", "국산").get();
        List<IncomingEntity> list = incomingJpaRepository.findAll();
        // then
        log.info("성공횟수 = {}, 실패 횟수 = {}",successCount, failCount);
        assertThat(list).hasSize(30);

        assertThat(inventory.getQuantity()).isEqualTo(30);
    }
}