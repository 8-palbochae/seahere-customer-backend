package com.seahere.backend.user.request;

import com.seahere.backend.common.entity.Address;
import com.seahere.backend.common.entity.Role;
import com.seahere.backend.common.entity.SocialType;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.domain.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CustomerSignupReq {
    @NotBlank(message = "이메일은 필수 입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 입니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력 입니다.")
    private String username;

    private Address address;

    private String telNumber;

    private SocialType socialType;

    private String socialId;

    @Builder
    public CustomerSignupReq(String email, String password, String username, Address address, String telNumber, SocialType socialType, String socialId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.telNumber = telNumber;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public UserEntity toEntity(){
        return UserEntity.builder()
                .email(email)
                .password(password)
                .username(username)
                .address(address)
                .telNumber(telNumber)
                .socialType(socialType)
                .socialId(socialId)
                .role(Role.CUSTOMER)
                .status(UserStatus.APPROVED)
                .leaves(false)
                .build();
    }
}
