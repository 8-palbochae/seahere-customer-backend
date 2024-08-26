package com.seahere.backend.follow.service;

import com.seahere.backend.follow.repository.FollowJpaRepository;
import com.seahere.backend.follow.repository.FollowRepository;
import com.seahere.backend.inventory.controller.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@Sql(value = "/sql/inventory-service-test.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Slf4j
@SpringBootTest
public class FollowServiceTest {
    @Autowired
    FollowService followService;
    @Autowired
    FollowJpaRepository followJpaRepository;
    @Autowired
    FollowRepository followRepository;

    @Test
    @DisplayName("userId와 companyId를 통한 팔로우")
    void test1() throws Exception {
        //given
        Long companyId = 101L;
        Long userId = 1L;

        // when
    
    }

}
