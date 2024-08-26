package com.seahere.backend.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.seahere.backend.alarm.dto.AlarmToCompanyEvent;
import com.seahere.backend.alarm.dto.AlarmToCustomerEvent;
import com.seahere.backend.alarm.dto.AlarmToFollowerEvent;
import com.seahere.backend.alarm.entity.AlarmCustomerLogEntity;
import com.seahere.backend.alarm.entity.AlarmHistoryEntity;
import com.seahere.backend.alarm.entity.AlarmTokenEntity;
import com.seahere.backend.alarm.exception.TokenNotFoundException;
import com.seahere.backend.alarm.repository.AlarmCustomerLogJpaRepository;
import com.seahere.backend.alarm.repository.AlarmHistoryJpaRepository;
import com.seahere.backend.alarm.repository.AlarmJapRepository;
import com.seahere.backend.alarm.repository.AlarmRepository;
import com.seahere.backend.redis.entity.FCMToken;
import com.seahere.backend.redis.respository.FCMTokenRepository;
import com.seahere.backend.user.domain.UserEntity;
import com.seahere.backend.user.exception.UserNotFound;
import com.seahere.backend.redis.exception.RedisFCMNotFound;
import com.seahere.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlarmServiceImpl implements AlarmService {

    private final FirebaseMessaging firebaseMessaging;
    private final AlarmHistoryJpaRepository alarmHistoryJpaRepository;
    private final AlarmJapRepository alarmJapRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmCustomerLogJpaRepository alarmCustomerLogJpaRepository;
    private final FCMTokenRepository fcmTokenRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void pushAlarmToCustomer(AlarmToCustomerEvent event) throws FirebaseMessagingException {
        log.info("이벤트 구독 확인, 사용자 ID: {}", event.getUserId());
        UserEntity user = userRepository.findById(event.getUserId()).orElseThrow(UserNotFound::new);
        AlarmTokenEntity alarmToken = alarmJapRepository.findByUser(user)
                .orElseThrow(TokenNotFoundException::new);
        String token = alarmToken.getToken();
        sendMessage(token, event.getTitle(), event.getBody());
        alarmHistoryJpaRepository.save(AlarmHistoryEntity.builder()
                .userId(event.getUserId())
                .title(event.getTitle())
                .createTime(LocalDateTime.now())
                .body(event.getBody())
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void pushAlarmToCompanyUser(AlarmToCompanyEvent event) {
        log.info("이벤트 구독 확인, 회사 ID: {}", event.getCompanyId());

        List<AlarmTokenEntity> users = alarmRepository.findByCompanyUser(event.getCompanyId());
        for (AlarmTokenEntity user : users) {
            try {
                String token = user.getToken();
                sendMessage(token, event.getTitle(), event.getBody());

                alarmHistoryJpaRepository.save(AlarmHistoryEntity.builder()
                        .userId(user.getUser().getId())
                        .title(event.getTitle())
                        .createTime(LocalDateTime.now())
                        .body(event.getBody())
                        .build());

            } catch (FirebaseMessagingException e) {
                log.error("Failed to send message to user: " + user.getUser().getId(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void pushAlarmToFollower(AlarmToFollowerEvent event) {
        log.info("이벤트 구독 확인, 회사 ID: {}", event.getCompanyId());

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String fish = extractFish(event.getBody(), "세일중입니다.");
        String url = baseUrl + "/alarm/log?fish=" + fish;

        List<AlarmTokenEntity> all = alarmRepository.findByCompanyFlowerUser(event.getCompanyId());
        for (AlarmTokenEntity user : all) {
            try {
                String token = user.getToken();

                sendMessage(token, event.getTitle(), event.getBody(), url);

                alarmHistoryJpaRepository.save(AlarmHistoryEntity.builder()
                        .userId(user.getUser().getId())
                        .title(event.getTitle())
                        .body(event.getBody())
                        .createTime(LocalDateTime.now())
                        .build());

            } catch (FirebaseMessagingException e) {
                log.error("Failed to send message to user: " + user.getUser().getId(), e);
            }
        }
    }

    @Override
    public void sendMessage(String token, String title, String message) throws FirebaseMessagingException {
        firebaseMessaging.send(FcmMessage.makeMessage(token, title, message));
    }

    @Override
    public void sendMessage(String token, String title, String message, String url) throws FirebaseMessagingException {
        firebaseMessaging.send(FcmMessage.makeMessage(token, title, message, url));
    }

    @Override
    public void sendMessages(List<String> tokens, String title, String message) throws Exception {
        firebaseMessaging.sendMulticast(FcmMessage.makeMessages(tokens, title, message));
    }

    @Override
    public void saveToken(Long userId, String token) {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Optional<AlarmTokenEntity> alarmTokenOptional = alarmJapRepository.findByUser(user);
        if (alarmTokenOptional.isPresent()) {
            AlarmTokenEntity alarmToken = alarmTokenOptional.get();
            alarmToken.tokenUpdate(token);
            return;
        }
        alarmJapRepository.save(AlarmTokenEntity.builder()
                .user(user)
                .token(token)
                .build());
    }

    public void saveAlarmClickLog(String fishLog, Long userId) {
        UserEntity customer = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        alarmCustomerLogJpaRepository.save(AlarmCustomerLogEntity.builder()
                .customer(customer)
                .log(fishLog)
                .build());
    }

    private String extractFish(String input, String keyword) {
        int keywordIndex = input.indexOf(keyword);
        if (keywordIndex != -1) {
            return input.substring(0, keywordIndex).trim();
        }
        return input;
    }

    @Override
    public Slice<AlarmHistoryEntity> findByUserId(Long userId, Pageable pageable) {
        return alarmHistoryJpaRepository.findByUserId(userId, pageable);
    }
}
