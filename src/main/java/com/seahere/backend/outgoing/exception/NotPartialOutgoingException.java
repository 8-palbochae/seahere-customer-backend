package com.seahere.backend.outgoing.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class NotPartialOutgoingException extends SeaHereException {
    private static final String MESSAGE = "부분발송에 동의하지 않은 주문입니다.";

    public NotPartialOutgoingException() {
        super(MESSAGE);
    }

    public NotPartialOutgoingException(String message) {
        super(message);
    }

    public NotPartialOutgoingException(String message, Throwable cause) {
        super(message, cause);
    }


    @Override
    public int getStatusCode() {
        return 400;
    }
}
