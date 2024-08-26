package com.seahere.backend.alarm.dto;

import com.seahere.backend.alarm.controller.response.AlarmHistoryResponse;
import com.seahere.backend.alarm.entity.AlarmHistoryEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AlarmHistoryDto {
    Long id;
    String title;
    String body;
    LocalDateTime createTime;
    Long saleCompanyId;

    public static AlarmHistoryDto from(AlarmHistoryEntity alarmHistoryEntity){
        return AlarmHistoryDto.builder()
                .id(alarmHistoryEntity.getAlarmHistoryId())
                .title(alarmHistoryEntity.getTitle())
                .body(alarmHistoryEntity.getBody())
                .createTime(alarmHistoryEntity.getCreateTime())
                .saleCompanyId(alarmHistoryEntity.getSaleCompanyId())
                .build();
    }
}
