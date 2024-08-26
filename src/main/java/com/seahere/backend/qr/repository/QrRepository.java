package com.seahere.backend.qr.repository;

import com.seahere.backend.product.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Repository
@Slf4j
public class QrRepository {

    public byte[] createQrZip(List<ProductEntity> products) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (ProductEntity product : products) {
                try {
                    String imageUrl = product.getQr();
                    if (imageUrl != null) {
                        URL url = new URL(imageUrl);
                        try (InputStream inputStream = url.openStream()) {
                            ZipEntry zipEntry = new ZipEntry("qr_" + product.getProductName() + ".png");
                            zos.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) >= 0) {
                                zos.write(buffer, 0, length);
                            }
                            zos.closeEntry();
                        }
                    } else {
                        log.warn("QR 이미지 파일 경로가 없습니다: " + product.getProductId());
                    }
                } catch (Exception e) {
                    log.error("Product ID {} 처리 중 오류 발생: {}", product.getProductId(), e.getMessage());
                }
            }
            zos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("QR 코드 ZIP 파일 생성 중 오류 발생", e);
            throw e;
        }
    }
}