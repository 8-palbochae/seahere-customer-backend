package com.seahere.backend.common.dto;

import com.seahere.backend.common.entity.Role;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.domain.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLogin {
    private Long userId;
    private String email;
    private String password;
    private Long companyId;
    private String username;
    private String companyName;
    private UserStatus status;
    private Role role;

    @Builder
    public UserLogin(Long userId, String email, String password, Long companyId, String username, String companyName, UserStatus status, Role role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.companyId = companyId;
        this.username = username;
        this.companyName = companyName;
        this.status = status;
        this.role = role;
    }

    public static UserLogin from(UserEntity user, CompanyEntity company){
        if(company != null){
            return UserLogin.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .companyId(company.getId())
                    .companyName(company.getCompanyName())
                    .username(user.getUsername())
                    .status(user.getStatus())
                    .role(user.getRole())
                    .build();
        }

        return UserLogin.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }
}