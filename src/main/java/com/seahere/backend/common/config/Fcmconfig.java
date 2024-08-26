package com.seahere.backend.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class Fcmconfig {

    @Value("${firebase.certification}")
    private String resourcePath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            Resource resource = resourceLoader.getResource(resourcePath);
            try (InputStream aboutFirebaseFile = resource.getInputStream()) {
                FirebaseOptions options = FirebaseOptions
                        .builder()
                        .setCredentials(GoogleCredentials.fromStream(aboutFirebaseFile))
                        .build();
                return FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                log.error("Error loading Firebase credentials from path: {}", resourcePath, e);
                throw e;
            }
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
