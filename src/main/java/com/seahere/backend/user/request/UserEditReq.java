package com.seahere.backend.user.request;

import com.seahere.backend.common.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEditReq {
    private String password;
    private String telNumber;
    private Address address;

    @Builder
    public UserEditReq(String password, String telNumber, Address address) {
        this.password = password;
        this.telNumber = telNumber;
        this.address = address;
    }

    public void encode(String encodedPassword){
        this.password=encodedPassword;
    }
}
