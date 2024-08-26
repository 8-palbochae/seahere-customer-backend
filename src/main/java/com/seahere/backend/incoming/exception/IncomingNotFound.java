package com.seahere.backend.incoming.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class IncomingNotFound extends SeaHereException {
    private static final String MESSAGE = "입고 정보를 찾을 수 없습니다.";
    public IncomingNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}