package com.seahere.backend.outgoing.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class OutgoingNotFoundException extends SeaHereException {
    private static String MESSAGE = "존재하지 않는 출고 요청 번호입니다.";

    public OutgoingNotFoundException() {
        super(MESSAGE);
    }

    public OutgoingNotFoundException(String message) {
        super(message);
    }

    public OutgoingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
