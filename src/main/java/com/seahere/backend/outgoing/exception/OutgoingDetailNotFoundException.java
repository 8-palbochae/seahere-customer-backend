package com.seahere.backend.outgoing.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class OutgoingDetailNotFoundException extends SeaHereException {

    public static final String MESSAGE = "없는 출고상세 내역입니다.";

    public OutgoingDetailNotFoundException() {
        super(MESSAGE);
    }

    public OutgoingDetailNotFoundException(String message) {
        super(message);
    }

    public OutgoingDetailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
