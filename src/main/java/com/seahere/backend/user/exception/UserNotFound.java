package com.seahere.backend.user.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class UserNotFound extends SeaHereException {
    private final static String MESSAGE = "존재하는 사용자가 없습니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
