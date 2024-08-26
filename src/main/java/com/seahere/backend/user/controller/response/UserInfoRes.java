package com.seahere.backend.user.controller.response;

import com.google.firebase.auth.UserInfo;
import com.seahere.backend.common.entity.Address;
import com.seahere.backend.company.controller.response.CompanyResponse;
import com.seahere.backend.user.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoRes {
    private Long userId;
    private String userName;
    private Address address;
    private String telNumber;
    private String email;
    private String profileImg;
    private String role;
    private CompanyResponse company;

    @Builder
    public UserInfoRes(Long userId, String userName, Address address, String telNumber, String email, CompanyResponse company, String profileImg, String role) {
        this.userId = userId;
        this.userName = userName;
        this.address = address;
        this.telNumber = telNumber;
        this.email = email;
        this.company = company;
        this.profileImg = profileImg;
        this.role = role;
    }

    public static UserInfoRes from(UserEntity user) {
        if (user.getCompany()!=null){
            return UserInfoRes.builder()
                    .userId(user.getId())
                    .address(user.getAddress())
                    .userName(user.getUsername())
                    .email(user.getEmail())
                    .telNumber(user.getTelNumber())
                    .profileImg(user.getProfileImage())
                    .role(user.getRole().toString())
                    .company(CompanyResponse.from(user.getCompany()))
                    .build();
        }

        return UserInfoRes.builder()
                .userId(user.getId())
                .address(user.getAddress())
                .userName(user.getUsername())
                .email(user.getEmail())
                .profileImg(user.getProfileImage())
                .telNumber(user.getTelNumber())
                .build();
    }
}
