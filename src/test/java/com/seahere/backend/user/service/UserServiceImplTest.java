package com.seahere.backend.user.service;

import com.seahere.backend.common.entity.Address;
import com.seahere.backend.common.entity.Role;
import com.seahere.backend.common.entity.SocialType;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.domain.UserStatus;
import com.seahere.backend.user.exception.UserNotFound;
import com.seahere.backend.user.repository.UserRepository;
import com.seahere.backend.user.request.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest{
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void clear(){
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("CustomerSignupReq 클래스를 통해서 커스터머 일반 회원 가입이 가능하다")
    void test1() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        CustomerSignupReq signupReq = CustomerSignupReq.builder()
                .email("test@test.com")
                .username("여보소")
                .password("1234")
                .address(address)
                .build();
        //when
        Long userId = userService.signupCustomer(signupReq);

        //then
        UserEntity findUser = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        assertEquals(findUser.getId(),userId);
        assertEquals(findUser.getEmail(),signupReq.getEmail());
        assertTrue(encoder.matches("1234", findUser.getPassword()));
        assertEquals(findUser.getRole(), Role.CUSTOMER);
    }

    @Test
    @DisplayName("BrokerSignup 클래스를 통해서  브로커 일반 회원 가입이 가능하고 상태는 PENDING이다")
    void test2() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        BrokerSignupReq signupReq = BrokerSignupReq.builder()
                .email("test@test.com")
                .username("여보소")
                .password("1234")
                .address(address)
                .build();
        //when
        Long userId = userService.signupBroker(signupReq);

        //then
        UserEntity findUser = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        assertEquals(findUser.getId(),userId);
        assertEquals(findUser.getEmail(),signupReq.getEmail());
        assertTrue(encoder.matches("1234", findUser.getPassword()));
        assertEquals(findUser.getRole(), Role.EMPLOYEE);
        assertEquals(findUser.getStatus(), UserStatus.PENDING);
    }

    @Transactional
    @Test
    @DisplayName("CeoSignupReq 클래스를 통해서  브로커 어드민 회원 가입이 가능하다")
    void test3() throws Exception{
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        CompanyEntity company = CompanyEntity.builder()
                .companyName("여보소수산")
                .address(address)
                .registrationNumber("12345")
                .build();
        companyRepository.save(company);

        CeoSignupReq signupReq = CeoSignupReq.builder()
                .email("test@test.com")
                .username("여보소")
                .password("1234")
                .companyId(company.getId())
                .address(address)
                .build();
        //when
        Long userId = userService.signupCeo(signupReq);

        //then
        UserEntity findUser = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        CompanyEntity findCompany = findUser.getCompany();
        assertEquals(findUser.getId(),userId);
        assertEquals(findUser.getEmail(),signupReq.getEmail());
        assertTrue(encoder.matches("1234", findUser.getPassword()));
        assertEquals(findUser.getRole(), Role.ADMIN);
        assertEquals(findUser.getStatus(), UserStatus.APPROVED);

        assertEquals(findCompany.getId(),company.getId());
        assertEquals(findCompany.getCompanyName(),company.getCompanyName());
        assertEquals(findCompany.getRegistrationNumber(),company.getRegistrationNumber());
    }

    @Transactional
    @Test
    @DisplayName("OAuthSignupReq 클래스를 통해서 커스터머 소셜 회원 가입이 가능하다.")
    void test4() throws Exception{
        //given
        UserEntity user = UserEntity.builder()
                .role(Role.GUEST)
                .socialType(SocialType.GOOGLE)
                .socialId("test")
                .email("test@test.com")
                .leaves(false)
                .password(null)
                .profileImage(null)
                .build();
        userRepository.save(user);

        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        OAuthSignupReq signupReq = OAuthSignupReq.builder()
                .userId(user.getId())
                .username("여보소")
                .companyId(null)
                .address(address)
                .type("customer")
                .build();
        //when
        Long userId = userService.signupOauth(signupReq);

        //then
        UserEntity result = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        assertEquals(result.getId(),userId);
        assertEquals(result.getEmail(), "test@test.com");
        assertEquals(result.getRole(), Role.CUSTOMER);
        assertEquals(result.getStatus(), UserStatus.APPROVED);
        assertEquals(result.getAddress().getPostCode(),address.getPostCode());
        assertEquals(result.getAddress().getMainAddress(),address.getMainAddress());
        assertEquals(result.getAddress().getSubAddress(),address.getSubAddress());
        assertEquals(result.getSocialType(),SocialType.GOOGLE);
        assertEquals(result.getSocialId(),"test");
    }

    @Transactional
    @Test
    @DisplayName("OAuthSignupReq 클래스를 통해서 브로커 소셜 회원 가입이 가능하다.")
    void test5() throws Exception{
        //given
        UserEntity user = UserEntity.builder()
                .role(Role.GUEST)
                .socialType(SocialType.GOOGLE)
                .socialId("test")
                .email("test@test.com")
                .leaves(false)
                .password(null)
                .profileImage(null)
                .build();
        userRepository.save(user);

        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        OAuthSignupReq signupReq = OAuthSignupReq.builder()
                .userId(user.getId())
                .username("여보소")
                .companyId(null)
                .address(address)
                .type("broker")
                .build();
        //when
        Long userId = userService.signupOauth(signupReq);

        //then
        UserEntity result = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        assertEquals(result.getId(),userId);
        assertEquals(result.getEmail(), "test@test.com");
        assertEquals(result.getRole(), Role.EMPLOYEE);
        assertEquals(result.getStatus(), UserStatus.PENDING);
        assertEquals(result.getAddress().getPostCode(),address.getPostCode());
        assertEquals(result.getAddress().getMainAddress(),address.getMainAddress());
        assertEquals(result.getAddress().getSubAddress(),address.getSubAddress());
        assertEquals(result.getSocialType(),SocialType.GOOGLE);
        assertEquals(result.getSocialId(),"test");
    }

    @Transactional
    @Test
    @DisplayName("OAuthSignupReq 클래스를 통해서 어드민 소셜 회원 가입이 가능하다.")
    void test6() throws Exception{
        //given
        UserEntity user = UserEntity.builder()
                .role(Role.GUEST)
                .socialType(SocialType.GOOGLE)
                .socialId("test")
                .email("test@test.com")
                .leaves(false)
                .password(null)
                .profileImage(null)
                .build();
        userRepository.save(user);


        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        CompanyEntity company = CompanyEntity.builder()
                .companyName("여보소 수산")
                .registrationNumber("12345")
                .profileImage(null)
                .address(address)
                .build();
        companyRepository.save(company);

        OAuthSignupReq signupReq = OAuthSignupReq.builder()
                .userId(user.getId())
                .username("여보소")
                .companyId(company.getId())
                .address(address)
                .type("ceo")
                .build();
        //when
        Long userId = userService.signupOauth(signupReq);

        //then
        UserEntity result = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        CompanyEntity resultCompany = result.getCompany();
        assertEquals(result.getId(),userId);
        assertEquals(result.getEmail(), "test@test.com");
        assertEquals(result.getRole(), Role.ADMIN);
        assertEquals(result.getStatus(), UserStatus.APPROVED);
        assertEquals(result.getAddress().getPostCode(),address.getPostCode());
        assertEquals(result.getAddress().getMainAddress(),address.getMainAddress());
        assertEquals(result.getAddress().getSubAddress(),address.getSubAddress());
        assertEquals(result.getSocialType(),SocialType.GOOGLE);
        assertEquals(result.getSocialId(),"test");

        assertEquals(resultCompany.getId(),company.getId());
        assertEquals(resultCompany.getCompanyName(),company.getCompanyName());
        assertEquals(resultCompany.getRegistrationNumber(),company.getRegistrationNumber());
    }

    @Transactional
    @Test
    @DisplayName("CEO는 사원 이메일을 통해서 사원에게 회사 정보 접근 승인이 가능하다.")
    void test7() throws Exception {
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        CompanyEntity company = CompanyEntity.builder()
                .companyName("여보소 수산")
                .registrationNumber("12345")
                .profileImage(null)
                .address(address)
                .build();
        companyRepository.save(company);

        UserEntity ceo = UserEntity.builder()
                .role(Role.ADMIN)
                .company(company)
                .socialType(null)
                .socialId(null)
                .status(UserStatus.APPROVED)
                .email("admin@test.com")
                .leaves(false)
                .password("1234")
                .profileImage(null)
                .build();
        userRepository.save(ceo);

        UserEntity employee = UserEntity.builder()
                .role(Role.EMPLOYEE)
                .socialType(null)
                .socialId(null)
                .status(UserStatus.PENDING)
                .email("emp@test.com")
                .leaves(false)
                .password("1234")
                .profileImage(null)
                .build();
        userRepository.save(employee);

        //when
        userService.approveEmployee(ceo.getEmail(),employee.getEmail());

        UserEntity result = userRepository.findById(employee.getId())
                .orElseThrow(UserNotFound::new);

        //then
        assertNotNull(result.getCompany());
        assertEquals(result.getStatus(),UserStatus.APPROVED);
        assertEquals(result.getCompany().getCompanyName(),company.getCompanyName());
        assertEquals(result.getCompany().getRegistrationNumber(),company.getRegistrationNumber());
    }

    @Test
    @DisplayName("사용자 비밀번호는 수정 가능하다")
    void editUserPassword() throws Exception {
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        UserEntity customer = UserEntity.builder()
                .role(Role.CUSTOMER)
                .socialType(null)
                .socialId(null)
                .status(UserStatus.APPROVED)
                .email("test@test.com")
                .telNumber("01012341234")
                .leaves(false)
                .password("1234")
                .profileImage(null)
                .build();
        userRepository.save(customer);

        UserEditReq userEditReq = UserEditReq.builder()
                .password("5678")
                .build();

        //when
        userService.editUser(customer.getId(),userEditReq);

        UserEntity result = userRepository.findById(customer.getId())
                .orElseThrow(UserNotFound::new);
        //then
        assertTrue(encoder.matches("5678", result.getPassword()));
    }

    @Test
    @DisplayName("사용자 주소는 수정 가능하다")
    void editUserAddress() throws Exception {
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        UserEntity customer = UserEntity.builder()
                .role(Role.CUSTOMER)
                .socialType(null)
                .socialId(null)
                .status(UserStatus.APPROVED)
                .email("test@test.com")
                .telNumber("01012341234")
                .leaves(false)
                .password("1234")
                .profileImage(null)
                .build();
        userRepository.save(customer);

        Address editAddress = Address.builder()
                .postCode("123")
                .mainAddress("수정 주소")
                .subAddress("수정 상세 주소")
                .build();

        UserEditReq userEditReq = UserEditReq.builder()
                .address(editAddress)
                .build();

        //when
        userService.editUser(customer.getId(),userEditReq);

        UserEntity result = userRepository.findById(customer.getId())
                .orElseThrow(UserNotFound::new);
        //then
        assertEquals(result.getAddress().getPostCode(),editAddress.getPostCode());
        assertEquals(result.getAddress().getMainAddress(),editAddress.getMainAddress());
        assertEquals(result.getAddress().getSubAddress(),editAddress.getSubAddress());
    }

    @Test
    @DisplayName("사용자 전화번호는 수정 가능하다")
    void editUserTelNumber() throws Exception {
        //given
        Address address = Address.builder()
                .postCode("12345")
                .mainAddress("메인 주소")
                .subAddress("상세 주소")
                .build();

        UserEntity customer = UserEntity.builder()
                .role(Role.CUSTOMER)
                .socialType(null)
                .socialId(null)
                .status(UserStatus.APPROVED)
                .email("test@test.com")
                .telNumber("01012341234")
                .leaves(false)
                .password("1234")
                .profileImage(null)
                .build();
        userRepository.save(customer);

        UserEditReq userEditReq = UserEditReq.builder()
                .telNumber("01056785678")
                .build();

        //when
        userService.editUser(customer.getId(),userEditReq);

        UserEntity result = userRepository.findById(customer.getId())
                .orElseThrow(UserNotFound::new);
        //then
        assertEquals(result.getTelNumber(),userEditReq.getTelNumber());
    }
}

