package com.seahere.backend.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST"), CUSTOMER("ROLE_CUSTOMER"),
    ADMIN("ROLE_ADMIN"), EMPLOYEE("ROLE_EMPLOYEE");

    private final String key;
}
