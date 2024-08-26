package com.seahere.backend.incoming.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.common.exception.SeaHereException;
import com.seahere.backend.incoming.controller.request.IncomingDataRequest;
import com.seahere.backend.incoming.controller.request.IncomingEditReq;
import com.seahere.backend.incoming.service.IncomingService;
import com.seahere.backend.redis.service.IncomingLockFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class IncomingController {

    private final IncomingService incomingService;
    private final IncomingLockFacadeService incomingLockFacadeService;


    @PostMapping("/incoming")
    public ResponseEntity saveIncomingData(@RequestBody IncomingDataRequest incomingDataRequest,@AuthenticationPrincipal CustomUserDetails userDetails) throws InterruptedException {
        incomingLockFacadeService.save(userDetails.getUser().getCompanyId(), userDetails.getUser().getUserId(), incomingDataRequest);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/incoming")
    public ResponseEntity<Long> modifyIncomingData(@RequestBody IncomingEditReq IncomingEditReq) {
        Long incomingId = incomingService.editIncoming(IncomingEditReq);
        return ResponseEntity.ok(incomingId);
    }
}