package com.seahere.backend.ocr.controller.request;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;

public class MultipartFileRequestBody extends LinkedMultiValueMap<String, Object> {
    public MultipartFileRequestBody(byte[] fileBytes, String jsonMessage) {
        add("message", jsonMessage);
        add("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return "image.jpg";
            }
        });
    }
}