package com.seahere.backend.company.service;

import com.seahere.backend.common.entity.Address;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.exception.CompanyNotFound;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.company.controller.request.CompanyCreateReq;
import com.seahere.backend.company.controller.request.CompanySearch;
import com.seahere.backend.company.controller.response.CompanyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CompanyServiceImplTest {
    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    @DisplayName("CompanyCreateReq를 통해서 회사 등록이 가능하다.")
    void test1() throws Exception {
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        CompanyCreateReq companyCreateReq = CompanyCreateReq.builder()
                .companyName("여보소수산")
                .registrationNumber("123456")
                .address(address)
                .build();
        //when
        Long savedId = companyService.save(companyCreateReq);
        CompanyEntity result = companyRepository.findById(savedId).orElseThrow(CompanyNotFound::new);

        //then
        assertEquals(savedId, result.getId());
        assertEquals("여보소수산", result.getCompanyName());
        assertEquals("123456", result.getRegistrationNumber());
    }

    @Test
    @DisplayName("Company ID로 회사 정보 조회가 가능하다")
    void test2() throws Exception {
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        CompanyEntity company = CompanyEntity.builder()
                .companyName("여보소수산")
                .registrationNumber("123456")
                .address(address)
                .build();

        companyRepository.save(company);
        //when
        CompanyResponse result = companyService.getCompanyById(company.getId());

        //then
        assertEquals(result.getCompanyName(), "여보소수산");
        assertEquals(result.getRegistrationNumber(), "123456");
    }

    @Test
    @DisplayName("사업자 번호로 회사 정보 조회가 가능하다")
    void test3() throws Exception {
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("부산광역시")
                .subAddress("스파로스 아카데미")
                .build();

        CompanyEntity company = CompanyEntity.builder()
                .companyName("여보소수산")
                .registrationNumber("123456")
                .address(address)
                .build();

        companyRepository.save(company);
        //when
        CompanyResponse result = companyService.getCompanyByRegNumber("123456");

        //then
        assertEquals(result.getCompanyName(), "여보소수산");
        assertEquals(result.getRegistrationNumber(), "123456");
    }

    @Test
    @DisplayName("회사 정보 페이지 1페이지 조회 테스트")
    void test4() throws Exception {
        //given
        List<CompanyEntity> requestCompanies = IntStream.range(1, 31)
                .mapToObj(i -> CompanyEntity.builder()
                        .companyName("여보소수산" + i)
                        .registrationNumber(String.valueOf(i))
                        .build())
                .collect(Collectors.toList());

        companyRepository.saveAll(requestCompanies);

        CompanySearch companySearch = CompanySearch.builder()
                .page(1)
                .size(10)
                .build();

        //when
        List<CompanyResponse> result = companyService.getList(companySearch);

        //then
        assertEquals(10, result.size());
        assertEquals("여보소수산30", result.get(0).getCompanyName());
        assertEquals("30", result.get(0).getRegistrationNumber());
    }

    @Test
    @DisplayName("회사 정보 페이지 1페이지 조회 시 0이 들어가는 회사만 테스트")
    void test5() throws Exception {
        //given
        List<CompanyEntity> requestCompanies = IntStream.range(1, 31)
                .mapToObj(i -> CompanyEntity.builder()
                        .companyName("여보소수산" + i)
                        .registrationNumber(String.valueOf(i))
                        .build())
                .collect(Collectors.toList());

        companyRepository.saveAll(requestCompanies);

        CompanySearch companySearch = CompanySearch.builder()
                .page(1)
                .size(10)
                .searchWord("10")
                .build();

        //when
        List<CompanyResponse> result = companyService.getList(companySearch);

        //then
        assertEquals(1, result.size());
        assertEquals("여보소수산10", result.get(0).getCompanyName());
        assertEquals("10", result.get(0).getRegistrationNumber());
    }
}