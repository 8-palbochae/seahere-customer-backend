package com.seahere.backend.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmToFollowerEvent {
    private Long companyId;
    private String title;
    private String body;
}
