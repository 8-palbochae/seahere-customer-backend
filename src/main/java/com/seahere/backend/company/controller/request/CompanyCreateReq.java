package com.seahere.backend.company.controller.request;

import com.seahere.backend.common.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyCreateReq {
    private String registrationNumber;
    private String representativeName;
    private String companyName;
    private Address address;
    private String profileImage;

    @Builder
    public CompanyCreateReq(String registrationNumber, String representativeName, String companyName, Address address, String profileImage) {
        this.registrationNumber = registrationNumber;
        this.representativeName = representativeName;
        this.companyName = companyName;
        this.address = address;
        this.profileImage = profileImage;
    }
}
