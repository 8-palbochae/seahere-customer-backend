package com.seahere.backend.user.controller.response;

import com.seahere.backend.common.entity.Address;
import com.seahere.backend.company.controller.response.EmployeeResponse;
import com.seahere.backend.company.entity.CompanyEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SettingCompanyResponse {
    private Long id;
    private String registrationNumber;
    private String companyName;
    private Address address;
    private String profileImage;
    private List<EmployeeResponse> employeeList;

    @Builder
    public SettingCompanyResponse(Long id, String registrationNumber, String companyName, Address address, String profileImage,List<EmployeeResponse> employeeList) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.companyName = companyName;
        this.address = address;
        this.profileImage = profileImage;
        this.employeeList = employeeList;
    }

    public static SettingCompanyResponse from(CompanyEntity company){
        return SettingCompanyResponse.builder()
                .id(company.getId())
                .registrationNumber(company.getRegistrationNumber())
                .companyName(company.getCompanyName())
                .address(company.getAddress())
                .profileImage(company.getProfileImage())
                .employeeList(company.getUserEntityList().stream().map(EmployeeResponse::from).collect(Collectors.toList()))
                .build();
    }
}
