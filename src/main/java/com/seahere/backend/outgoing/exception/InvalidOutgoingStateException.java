package com.seahere.backend.outgoing.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class InvalidOutgoingStateException extends SeaHereException {
    private final static String MESSAGE = "잘못된 상태요청 입니다.";
    public InvalidOutgoingStateException() {
        super(MESSAGE);
    }

    public InvalidOutgoingStateException(String message) {
        super(message);
    }

    public InvalidOutgoingStateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
