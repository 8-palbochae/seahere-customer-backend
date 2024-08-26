package com.seahere.backend.alarm;

import com.seahere.backend.alarm.dto.AlarmToCustomerEvent;
import com.seahere.backend.alarm.entity.AlarmTokenEntity;
import com.seahere.backend.alarm.repository.AlarmJapRepository;
import com.seahere.backend.alarm.service.AlarmService;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AlarmServiceImplTest {

    @Autowired
    private AlarmJapRepository alarmJapRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlarmService alarmService;

    @Test
    @DisplayName("알람발송테스트")
    void sendMessage() throws Exception {
    }
}