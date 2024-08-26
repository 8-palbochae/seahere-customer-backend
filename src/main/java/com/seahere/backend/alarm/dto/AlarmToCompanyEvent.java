package com.seahere.backend.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmToCompanyEvent {
    private Long CompanyId;
    private String title;
    private String body;
}
