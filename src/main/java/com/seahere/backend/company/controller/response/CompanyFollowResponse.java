package com.seahere.backend.company.controller.response;

import com.seahere.backend.common.entity.Address;
import com.seahere.backend.company.entity.CompanyEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CompanyFollowResponse {
    private Long id;
    private String registrationNumber;
    private String companyName;
    private Address address;
    private String profileImage;
    private boolean isFollowed;

    @Builder
    public CompanyFollowResponse(Long id, String registrationNumber, String companyName, Address address, String profileImage, boolean isFollowed) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.companyName = companyName;
        this.address = address;
        this.profileImage = profileImage;
        this.isFollowed = isFollowed;
    }

    public static CompanyFollowResponse from(CompanyEntity company, boolean isFollowed) {
        if (company == null) {
            return null;
        }

        return CompanyFollowResponse.builder()
                .id(company.getId())
                .registrationNumber(company.getRegistrationNumber())
                .companyName(company.getCompanyName())
                .address(company.getAddress())
                .profileImage(company.getProfileImage())
                .isFollowed(isFollowed)
                .build();
    }
}
