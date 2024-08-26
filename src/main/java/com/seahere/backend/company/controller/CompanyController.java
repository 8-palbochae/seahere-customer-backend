package com.seahere.backend.company.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.company.controller.request.CompanyCreateReq;
import com.seahere.backend.company.controller.request.CompanySearch;
import com.seahere.backend.company.controller.response.CompanyFollowResponse;
import com.seahere.backend.company.controller.response.CompanyResponse;
import com.seahere.backend.company.exception.DuplicateCompanyException;
import com.seahere.backend.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<Long> companyAdd(@RequestBody CompanyCreateReq companyCreateReq) {
        Long savedId = companyService.save(companyCreateReq);
        return ResponseEntity.ok(savedId);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getList(@ModelAttribute CompanySearch companySearch) {
        List<CompanyResponse> companyResponses = companyService.getList(companySearch);
        return ResponseEntity.ok(companyResponses);
    }

    @GetMapping("/trade/companies")
    public ResponseEntity<List<CompanyResponse>> getTradeList(@ModelAttribute CompanySearch companySearch,  @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CompanyResponse> companyResponses = companyService.getTradeCompany(companySearch,userDetails.getUser().getCompanyId());
        return ResponseEntity.ok(companyResponses);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long companyId) {
        CompanyResponse companyResponse = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(companyResponse);
    }

    @GetMapping("/best")
    public ResponseEntity<CompanyResponse> getBestCompany() {
        CompanyResponse mostOutgoingCompany = companyService.getMostOutgoingCompany();
        return ResponseEntity.ok(mostOutgoingCompany);
    }

    @GetMapping("/customer")
    public ResponseEntity<List<CompanyFollowResponse>> getListForCustomer(@ModelAttribute CompanySearch companySearch,
                                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<CompanyFollowResponse> companyResponses = companyService.getListForCustomer(customUserDetails.getUser().getUserId(), companySearch);
        return ResponseEntity.ok(companyResponses);
    }

    @GetMapping("/customer/follow")
    public ResponseEntity<?> getFollowListForCustomer(
            @ModelAttribute CompanySearch companySearch,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            List<CompanyFollowResponse> companyResponses = companyService.getFollowListForCustomer(customUserDetails.getUser().getUserId(), companySearch);
            return ResponseEntity.ok(companyResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @GetMapping("/customer/{companyId}")
    public ResponseEntity<CompanyFollowResponse> getCompanyForCustomer(@PathVariable Long companyId,
                                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CompanyFollowResponse companyResponse = companyService.getCompanyByIdForCustomer(customUserDetails.getUser().getUserId(), companyId);
        return ResponseEntity.ok(companyResponse);
    }

    @GetMapping("/customer/best")
    public ResponseEntity<CompanyFollowResponse> getBestCompanyForCustomer(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CompanyFollowResponse mostOutgoingCompany = companyService.getMostOutgoingCompanyForCustomer(customUserDetails.getUser().getUserId());
        return ResponseEntity.ok(mostOutgoingCompany);
    }

   @PostMapping("/duplicate")
   public ResponseEntity<Void> checkDuplicateCompany(@RequestBody Map<String,String> registrationNumber) {
        companyService.duplicateRegNumber(registrationNumber.get("registrationNumber"));
        return ResponseEntity.ok(null);
    }

}
