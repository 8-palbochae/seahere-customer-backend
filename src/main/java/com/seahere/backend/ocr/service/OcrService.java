package com.seahere.backend.ocr.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seahere.backend.ocr.controller.response.ExtractOcrResponse;
import com.seahere.backend.ocr.repository.OcrRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class OcrService {

    private final OcrRepository ocrRepository;

    public OcrService(OcrRepository ocrRepository) {
        this.ocrRepository = ocrRepository;
    }

    public ExtractOcrResponse processOcr(MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();

            String ocrResponseJson = ocrRepository.callOcrApi(imageBytes);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(ocrResponseJson);

            ExtractOcrResponse ocrResponse = new ExtractOcrResponse();

            JsonNode resultNode = rootNode.path("images").path(0).path("bizLicense").path("result");
            if (!resultNode.isMissingNode()) {
                ocrResponse.setCompanyName(resultNode.path("corpName").path(0).path("text").asText(null));
                ocrResponse.setRepresentativeName(resultNode.path("repName").path(0).path("text").asText(null));
                ocrResponse.setBusinessNumber(resultNode.path("registerNumber").path(0).path("text").asText(null));
                ocrResponse.setAddress(resultNode.path("bisAddress").path(0).path("text").asText(null));
                ocrResponse.setOpenDate(resultNode.path("openDate").path(0).path("text").asText(null));
            } else {
                log.warn("resultNode is missing in OCR response");
            }

            return ocrResponse;

        } catch (IOException e) {
            log.error("Error processing OCR", e);
            throw new RuntimeException("Failed to process OCR", e);
        }
    }

}
