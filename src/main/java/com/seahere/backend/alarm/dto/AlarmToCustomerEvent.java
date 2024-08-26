package com.seahere.backend.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmToCustomerEvent {
    private Long userId;
    private String title;
    private String body;

}
