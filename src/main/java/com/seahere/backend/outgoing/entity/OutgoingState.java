package com.seahere.backend.outgoing.entity;

import com.seahere.backend.outgoing.exception.InvalidOutgoingStateException;

public enum OutgoingState {
    PENDING("출고요청"), READY("출고대기"), COMPLETE("출고완료"), REJECT("출고거절");

    private String text;

    OutgoingState(String text) {
        this.text = text;
    }

    public String printState(){
        return this.text;
    }

    public static OutgoingState from(String state) {
        if (state == null) {
            throw new InvalidOutgoingStateException();
        }
        switch (state.toLowerCase()) {
            case "ready":
                return READY;
            case "reject":
                return REJECT;
            case "complete":
                return COMPLETE;
            default:
                throw new InvalidOutgoingStateException();
        }
    }
}
