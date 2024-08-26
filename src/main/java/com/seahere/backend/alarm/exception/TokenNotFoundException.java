package com.seahere.backend.alarm.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class TokenNotFoundException extends SeaHereException {
    public final static String MESSAGE = "해당 유저의 토큰 정보가 없습니다.";

    public TokenNotFoundException() {
        super(MESSAGE);
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
