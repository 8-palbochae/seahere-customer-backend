package com.seahere.backend.follow.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.company.controller.response.CompanyFollowResponse;
import com.seahere.backend.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow-company")
    public ResponseEntity<Void> followCompany(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long companyId) {
        followService.save(customUserDetails.getUser().getUserId(), companyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unfollow-company")
    public ResponseEntity<Void> unfollowCompany(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long companyId) {
        followService.delete(customUserDetails.getUser().getUserId(), companyId);
        return ResponseEntity.ok().build();
    }
}
