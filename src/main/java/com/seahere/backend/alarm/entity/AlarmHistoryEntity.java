package com.seahere.backend.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alarm_history")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlarmHistoryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmHistoryId;
    private Long userId;
    private String title;
    private String body;
    private LocalDateTime createTime;
    private Long SaleCompanyId;


}
