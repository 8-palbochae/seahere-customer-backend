package com.seahere.backend.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seahere.backend.common.entity.Address;
import com.seahere.backend.user.repository.UserRepository;
import com.seahere.backend.user.request.BrokerSignupReq;
import com.seahere.backend.user.request.CustomerSignupReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clear(){
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("CusomterSignupReq 클래스로 POST 요청시 커스터머 계정이 생성된다")
    public void test1() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        CustomerSignupReq signupReq = CustomerSignupReq.builder()
                .email("test@test.com")
                .username("여보소")
                .password("1234")
                .address(address)
                .build();

        String json = objectMapper.writeValueAsString(signupReq);
        //expect
        mockMvc.perform(MockMvcRequestBuilders. post("/users/customer")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("CusomterSignupReq 클래스로 POST 요청시 이메일이 없으면 400을 반환 한다")
    void test2() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        CustomerSignupReq signupReq = CustomerSignupReq.builder()
                .username("여보소")
                .password("1234")
                .address(address)
                .build();

        String json = objectMapper.writeValueAsString(signupReq);
        //expect
        mockMvc.perform(MockMvcRequestBuilders. post("/users/customer")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @DisplayName("CusomterSignupReq 클래스로 POST 요청시 비밀번호가 없으면 400을 반환 한다")
    void test3() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        CustomerSignupReq signupReq = CustomerSignupReq.builder()
                .username("여보소")
                .email("test@test.com")
                .address(address)
                .build();

        String json = objectMapper.writeValueAsString(signupReq);
        //expect
        mockMvc.perform(MockMvcRequestBuilders. post("/users/customer")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @DisplayName("CusomterSignupReq 클래스로 POST 요청시 이름이 없으면 400를 반환 한다")
    void test4() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        CustomerSignupReq signupReq = CustomerSignupReq.builder()
                .email("test@test.com")
                .password("1234")
                .address(address)
                .build();

        String json = objectMapper.writeValueAsString(signupReq);
        //expect
        mockMvc.perform(MockMvcRequestBuilders. post("/users/customer")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("BrokerSignupReq 클래스로 POST 요청시 브로커 계정이 대기 상태로 생성된다")
    public void test5() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        BrokerSignupReq signupReq = BrokerSignupReq.builder()
                .email("test@test.com")
                .username("여보소")
                .password("1234")
                .address(address)
                .build();

        String json = objectMapper.writeValueAsString(signupReq);
        //expect
        mockMvc.perform(MockMvcRequestBuilders. post("/users/broker")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andDo(print());
    }
}