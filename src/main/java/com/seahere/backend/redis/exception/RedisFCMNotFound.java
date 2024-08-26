package com.seahere.backend.redis.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class RedisFCMNotFound extends SeaHereException {
    private static final String MESSAGE = "리프레시 토큰이 존재하지 않습니다.";

    public RedisFCMNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
