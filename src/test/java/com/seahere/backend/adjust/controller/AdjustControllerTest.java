package com.seahere.backend.adjust.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.common.entity.Role;
import com.seahere.backend.common.dto.UserLogin;
import com.seahere.backend.adjust.controller.request.AdjustRequest;
import com.seahere.backend.inventory.controller.request.InventoryReqDetailSearchRequest;
import com.seahere.backend.inventory.repository.InventoryRepository;
import com.seahere.backend.inventory.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Sql(value = "/sql/inventory-service-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class AdjustControllerTest {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private CustomUserDetails getCustomUserDetails() {
        UserLogin userLogin = UserLogin.builder()
                .companyId(101L)
                .username("testUser")
                .password("testPass")
                .role(Role.EMPLOYEE)
                .build();
        return new CustomUserDetails(userLogin);
    }

    @Test
    @DisplayName("조정 시 현재 재고량이 변경된다.")
    public void test1() throws Exception {
        //given
        InventoryReqDetailSearchRequest inventoryReqDetailSearchRequest = InventoryReqDetailSearchRequest.builder()
                .size(10)
                .page(0)
                .name("광어")
                .category("활어")
                .build();

        mockMvc.perform(get("/inventories/details")
                        .param("size", String.valueOf(inventoryReqDetailSearchRequest.getSize()))
                        .param("page", String.valueOf(inventoryReqDetailSearchRequest.getPage()))
                        .param("name", inventoryReqDetailSearchRequest.getName())
                        .param("category", inventoryReqDetailSearchRequest.getCategory())
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());


        AdjustRequest adjustRequest = AdjustRequest.builder()
                .inventoryId(1L)
                .afterQuantity(100.0f)
                .reason("폐기")
                .build();

        String adjustRequestJson = objectMapper.writeValueAsString(adjustRequest);

        //expect
        mockMvc.perform(post("/adjust")
                        .content(adjustRequestJson)
                        .contentType(APPLICATION_JSON)
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/inventories/details")
                        .param("size", String.valueOf(inventoryReqDetailSearchRequest.getSize()))
                        .param("page", String.valueOf(inventoryReqDetailSearchRequest.getPage()))
                        .param("name", inventoryReqDetailSearchRequest.getName())
                        .param("category", inventoryReqDetailSearchRequest.getCategory())
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("companyId에 따른 조정 테이블 조회.")
    public void test2() throws Exception {
        //given
        AdjustRequest adjustRequest = AdjustRequest.builder()
                .inventoryId(1L)
                .afterQuantity(100.0f)
                .reason("폐기")
                .build();

        String adjustRequestJson = objectMapper.writeValueAsString(adjustRequest);

        mockMvc.perform(post("/adjust")
                        .content(adjustRequestJson)
                        .contentType(APPLICATION_JSON)
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
