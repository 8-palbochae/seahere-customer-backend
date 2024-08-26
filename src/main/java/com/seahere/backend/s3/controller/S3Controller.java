package com.seahere.backend.s3.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.s3.service.S3Service;
import com.seahere.backend.user.domain.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/s3/profile")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PatchMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserEntity user = s3Service.getUserById(customUserDetails.getUser().getUserId());
        if(user.getProfileImage()!= null){
            String fileName = user.getProfileImage();
            s3Service.deleteFile(fileName);
        }
        String fileUrl = s3Service.uploadFileAndUpdateUserProfile(file, customUserDetails.getUser().getUserId());
        return ResponseEntity.ok(fileUrl);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserEntity user = s3Service.getUserById(customUserDetails.getUser().getUserId());
        if(user.getProfileImage()!= null){
            String fileName = user.getProfileImage();
            s3Service.deleteFile(fileName);
            user.updateProfileImage(null);
            s3Service.saveUser(user);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile-image-url")
    public ResponseEntity<String> getProfileImageUrl(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String fileUrl = s3Service.getUserProfileImageUrl(customUserDetails.getUser().getUserId());
        return ResponseEntity.ok(fileUrl);
    }

    @PatchMapping("/company/upload")
    public ResponseEntity<String> uploadFileForCompany(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CompanyEntity company = s3Service.getCompanyById(customUserDetails.getUser().getCompanyId());
        if(company.getProfileImage()!= null){
            String fileName = company.getProfileImage();
            s3Service.deleteFile(fileName);
        }
        String fileUrl = s3Service.uploadFileAndUpdateCompanyProfile(file, customUserDetails.getUser().getCompanyId());
        return ResponseEntity.ok(fileUrl);
    }

    @DeleteMapping("/company/delete")
    public ResponseEntity<Void> deleteFileForCompany(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CompanyEntity company = s3Service.getCompanyById(customUserDetails.getUser().getCompanyId());
        if(company.getProfileImage()!= null){
            String fileName = company.getProfileImage();
            s3Service.deleteFile(fileName);
            company.editProfileImage(null);
            s3Service.saveCompany(company);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.noContent().build();
    }
}
