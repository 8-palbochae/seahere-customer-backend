package com.seahere.backend.product.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class ProductNotFoundException extends SeaHereException {
    private static final String MESSAGE = "제품을 찾을 수 없습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
