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
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CeoSignupReq {
    @NotBlank(message = "이메일은 필수 입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 입니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력 입니다.")
    private String username;

    @NotNull(message = "CEO는 회사 ID를 반드시 가지고 있어야 합니다.")
    private Long companyId;

    private Address address;

    private String telNumber;

    private SocialType socialType;

    private String socialId;

    @Builder
    public CeoSignupReq(String email, String password, String username, Long companyId, Address address, String telNumber, SocialType socialType, String socialId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.companyId = companyId;
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
                .role(Role.ADMIN)
                .status(UserStatus.APPROVED)
                .leaves(false)
                .build();
    }
}
