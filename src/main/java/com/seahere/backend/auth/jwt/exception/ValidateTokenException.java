package com.seahere.backend.auth.jwt.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class ValidateTokenException extends SeaHereException {
    private static final String MESSAGE = "토큰이 만료 되었습니다.";

    public ValidateTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
