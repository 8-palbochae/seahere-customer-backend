package com.seahere.backend.company.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class DuplicateCompanyException extends SeaHereException {
    private static final String MESSAGE = "이미 등록된 회사입니다";
    public DuplicateCompanyException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
