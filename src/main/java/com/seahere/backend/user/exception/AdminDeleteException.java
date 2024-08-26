package com.seahere.backend.user.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class AdminDeleteException extends SeaHereException {
    public static final String MESSAGE = "관리자는 삭제할 수 없습니다.";

    public AdminDeleteException() {
        super(MESSAGE);
    }

    public AdminDeleteException(String message) {
        super(message);
    }

    public AdminDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
