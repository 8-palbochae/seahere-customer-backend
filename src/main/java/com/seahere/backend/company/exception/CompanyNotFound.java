package com.seahere.backend.company.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class CompanyNotFound extends SeaHereException {
    private static String MESSAGE = "존재하는 회사가 없습니다.";

    public CompanyNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
