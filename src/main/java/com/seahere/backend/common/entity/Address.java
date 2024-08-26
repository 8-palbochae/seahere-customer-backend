package com.seahere.backend.common.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Embeddable;


@Embeddable
@NoArgsConstructor
@Getter
public class Address {
    private String postCode;
    private String mainAddress;
    private String subAddress;

    @Builder
    public Address(String postCode, String mainAddress, String subAddress) {
        this.postCode = postCode;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
    }
}

