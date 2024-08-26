package com.seahere.backend.user.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.user.controller.response.SettingCompanyResponse;
import com.seahere.backend.company.service.CompanyService;
import com.seahere.backend.user.controller.request.EmployeeAddRequest;
import com.seahere.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final UserService userService;
    private final CompanyService companyService;

    @PatchMapping("/teams")
    public ResponseEntity<Void> memberApprove(@RequestBody EmployeeAddRequest employeeEmail, @AuthenticationPrincipal CustomUserDetails userDetails){
        String loginEmail = userDetails.getUser().getEmail();
        userService.approveEmployee(loginEmail,employeeEmail.getEmployeeEmail());

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/teams/{userId}")
    public void deleteEmployee(@PathVariable("userId")Long userId){
        userService.deleteEmployee(userId);
    }
    @GetMapping("/teams")
    public ResponseEntity<SettingCompanyResponse> getCompany(@AuthenticationPrincipal CustomUserDetails userDetails){

        SettingCompanyResponse response = companyService.getCompanyAndEmployee(userDetails.getUser().getCompanyId());
        return ResponseEntity.ok(response);
    }
}
