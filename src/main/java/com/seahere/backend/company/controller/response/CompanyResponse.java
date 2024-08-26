package com.seahere.backend.company.controller.response;

import com.seahere.backend.common.entity.Address;
import com.seahere.backend.company.entity.CompanyEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyResponse {
    private Long id;
    private String registrationNumber;
    private String companyName;
    private Address address;
    private String profileImage;

    @Builder
    public CompanyResponse(Long id, String registrationNumber, String companyName, Address address, String profileImage) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.companyName = companyName;
        this.address = address;
        this.profileImage = profileImage;
    }

    public static CompanyResponse from(CompanyEntity company) {
        if (company == null) {
            return null;
        }

        return CompanyResponse.builder()
                .id(company.getId())
                .registrationNumber(company.getRegistrationNumber())
                .companyName(company.getCompanyName())
                .address(company.getAddress())
                .profileImage(company.getProfileImage())
                .build();
    }
}
