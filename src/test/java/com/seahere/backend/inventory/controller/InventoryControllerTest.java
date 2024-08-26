package com.seahere.backend.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.common.entity.Role; // Role 클래스 임포트
import com.seahere.backend.common.dto.UserLogin; // UserLogin 클래스 임포트
import com.seahere.backend.inventory.controller.request.InventoryReqDetailSearchRequest;
import com.seahere.backend.inventory.controller.request.InventoryReqSearchRequest;
import com.seahere.backend.inventory.repository.InventoryRepository;
import com.seahere.backend.inventory.service.InventoryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/inventory-service-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerTest {

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
    @Disabled
    @DisplayName("InventoryReqSearchRequest 클래스로 GET 요청시 CompanyId에 따른 전체 재고 목록이 반환된다.")
    public void test1() throws Exception {
        //given
        InventoryReqSearchRequest inventoryReqSearchRequest = InventoryReqSearchRequest.builder()
                .size(10)
                .page(0)
                .search("")
                .build();

        //expect
        mockMvc.perform(get("/inventories")
                        .param("size", String.valueOf(inventoryReqSearchRequest.getSize()))
                        .param("page", String.valueOf(inventoryReqSearchRequest.getPage()))
                        .param("search", inventoryReqSearchRequest.getSearch())
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Disabled
    @DisplayName("InventoryReqSearchRequest 클래스로 /detail에 GET 요청시 CompanyId, name, category에 따른 재고 목록이 반환된다.")
    public void test2() throws Exception {
        //given
        InventoryReqDetailSearchRequest inventoryReqDetailSearchRequest = InventoryReqDetailSearchRequest.builder()
                .size(10)
                .page(0)
                .name("광어")
                .category("활어")
                .build();

        //expect
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
    @Disabled
    @DisplayName("InventoryReqSearchRequest 클래스로 GET 요청시 CompanyId, search에 따른 검색 재고 목록이 반환된다.")
    public void test3() throws Exception {
        //given
        InventoryReqSearchRequest inventoryReqSearchRequest = InventoryReqSearchRequest.builder()
                .size(10)
                .page(0)
                .search("광어")
                .build();

        //expect
        mockMvc.perform(get("/inventories")
                        .param("size", String.valueOf(inventoryReqSearchRequest.getSize()))
                        .param("page", String.valueOf(inventoryReqSearchRequest.getPage()))
                        .param("search", inventoryReqSearchRequest.getSearch())
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Disabled
    @DisplayName("재고가 없을 경우 content에 null을 반환된다.")
    public void test4() throws Exception {
        //given
        InventoryReqSearchRequest inventoryReqSearchRequest = InventoryReqSearchRequest.builder()
                .size(10)
                .page(0)
                .search("")
                .build();

        //expect
        mockMvc.perform(get("/inventories")
                        .param("size", String.valueOf(inventoryReqSearchRequest.getSize()))
                        .param("page", String.valueOf(inventoryReqSearchRequest.getPage()))
                        .param("search", inventoryReqSearchRequest.getSearch())
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Disabled
    @DisplayName("재고보다 많은 페이지를 param으로 넣을 경우 content에 null을 반환된다.")
    public void test5() throws Exception {
        //given
        InventoryReqSearchRequest inventoryReqSearchRequest = InventoryReqSearchRequest.builder()
                .size(10)
                .page(3)
                .search("")
                .build();

        //expect
        mockMvc.perform(get("/inventories")
                        .param("size", String.valueOf(inventoryReqSearchRequest.getSize()))
                        .param("page", String.valueOf(inventoryReqSearchRequest.getPage()))
                        .param("search", inventoryReqSearchRequest.getSearch())
                        .with(user(getCustomUserDetails())))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
