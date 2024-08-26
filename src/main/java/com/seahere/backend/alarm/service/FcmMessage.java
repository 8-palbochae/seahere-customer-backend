package com.seahere.backend.alarm.service;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import java.util.List;

public class FcmMessage {

    public static Message makeMessage(String targetToken, String title, String body) {
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return Message
                .builder()
                .setNotification(notification)
                .setToken(targetToken)
                .build();
    }
    public static Message makeMessage(String targetToken, String title, String body, String url) {
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return Message
                .builder()
                .putData("url",url)
                .setNotification(notification)
                .setToken(targetToken)
                .build();
    }

    public static MulticastMessage makeMessages(List<String> targetTokens,String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(targetTokens)
                .build();

    }
}
