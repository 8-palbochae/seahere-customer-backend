package com.seahere.backend.user.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class DuplicateEmailException extends SeaHereException {
    private static final String MESSAGE = "중복된 이메일 입니다";
    public DuplicateEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
