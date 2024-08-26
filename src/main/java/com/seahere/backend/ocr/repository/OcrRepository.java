package com.seahere.backend.ocr.repository;

import com.seahere.backend.ocr.controller.request.MultipartFileRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
@PropertySource("classpath:ocrConfig.properties")
public class OcrRepository {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Environment env;

    public String callOcrApi(byte[] imageBytes) {
        String clovaOcrApiUrl = env.getProperty("clova.ocr.api.url");
        String clovaOcrSecretKey = env.getProperty("clova.ocr.secret.key");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data");
        headers.set("X-OCR-SECRET", clovaOcrSecretKey);

        String jsonMessage = "{\n" +
                "    \"version\": \"V2\",\n" +
                "    \"requestId\": \"uuid\",\n" +
                "    \"timestamp\": " + System.currentTimeMillis() + ",\n" +
                "    \"images\": [\n" +
                "        {\n" +
                "            \"format\": \"jpg\",\n" +
                "            \"name\": \"test\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        MultipartFileRequestBody multipartBody = new MultipartFileRequestBody(imageBytes, jsonMessage);
        HttpEntity<MultipartFileRequestBody> requestEntity = new HttpEntity<>(multipartBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                clovaOcrApiUrl, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }
}
