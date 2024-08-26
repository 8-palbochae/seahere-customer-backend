package com.seahere.backend.inventory.exception;

import com.seahere.backend.common.exception.SeaHereException;

public class InventoryNotFoundException extends SeaHereException {
    private final static String MESSAGE = "해당하는 재고가 없습니다.";

    public InventoryNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}