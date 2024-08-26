package com.seahere.backend.outgoing.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class LackInventoryException extends SeaHereException {

    public static final String MESSAGE = "보유 재고가 요청 재고보다 부족합니다.";

    public LackInventoryException() {
        super(MESSAGE);
    }

    public LackInventoryException(String message) {
        super(message);
    }

    public LackInventoryException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
