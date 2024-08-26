package com.seahere.backend.company.controller.response;

import com.seahere.backend.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EmployeeResponse {

    private Long userId;
    private String profileImg;
    private String userName;
    private String email;

    public static EmployeeResponse from(UserEntity user){
        return EmployeeResponse.builder()
                .userId(user.getId())
                .profileImg(user.getProfileImage())
                .userName(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
