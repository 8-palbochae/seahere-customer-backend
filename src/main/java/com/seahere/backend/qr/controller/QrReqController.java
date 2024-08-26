package com.seahere.backend.qr.controller;

import com.seahere.backend.qr.service.QrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/sendQR")
public class QrReqController {

    private final QrService qrService;

    @PostMapping
    public ResponseEntity<byte[]> sendQR(@RequestBody List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            byte[] zipData = qrService.createQrZip(productIds);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qr_codes.zip");

            return new ResponseEntity<>(zipData, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to create QR ZIP file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
