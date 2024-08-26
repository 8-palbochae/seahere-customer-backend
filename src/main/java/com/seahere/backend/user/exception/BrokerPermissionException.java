package com.seahere.backend.user.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class BrokerPermissionException extends SeaHereException {
    private static String MESSAGE = "허가된 브로커가 아닙니다.";

    public BrokerPermissionException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
