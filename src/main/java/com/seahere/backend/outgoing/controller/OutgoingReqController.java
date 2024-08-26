package com.seahere.backend.outgoing.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.outgoing.controller.request.OutgoingCreateReq;
import com.seahere.backend.outgoing.controller.request.OutgoingReqSearchRequest;
import com.seahere.backend.outgoing.controller.request.OutgoingSearchReq;
import com.seahere.backend.outgoing.controller.request.OutgoingStateChangeRequest;
import com.seahere.backend.outgoing.controller.response.CustomerOutgoingDetailRes;
import com.seahere.backend.outgoing.controller.response.OutgoingCallListResponse;
import com.seahere.backend.outgoing.controller.response.OutgoingDetailResponse;
import com.seahere.backend.outgoing.controller.response.OutgoingRes;
import com.seahere.backend.outgoing.controller.response.OutgoingTodayRes;
import com.seahere.backend.outgoing.dto.OutgoingCallDto;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import com.seahere.backend.outgoing.entity.OutgoingState;
import com.seahere.backend.outgoing.service.OutgoingDetailService;
import com.seahere.backend.outgoing.service.OutgoingService;
import com.seahere.backend.redis.service.OutgoingLockFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.IconUIResource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/outgoings")
@RequiredArgsConstructor
public class OutgoingReqController {

    private final OutgoingService outgoingService;
    private final OutgoingDetailService outgoingDetailService;
    private final OutgoingLockFacadeService outgoingLockFacadeService;

    @PostMapping("")
    public ResponseEntity<Void> outgoingCreate(@RequestBody OutgoingCreateReq outgoingCreateReq, @AuthenticationPrincipal CustomUserDetails userDetails){
        outgoingService.save(outgoingCreateReq,userDetails.getUser().getUserId());
        return ResponseEntity.ok(null);
    }

    @GetMapping()
    public ResponseEntity<OutgoingCallListResponse> outgoingReqList(OutgoingReqSearchRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){
        log.info("size = {} page = {}",request.getSize(),request.getPage());
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "outgoingId"));
        Slice<OutgoingEntity> results = outgoingService.findByOutgoingStateIsPending(userDetails.getUser().getCompanyId(), pageRequest
                , request.getStartDate(), request.getEndDate(), request.getSearch());
        OutgoingCallListResponse outgoingCallListResponse = new OutgoingCallListResponse(results);
        return ResponseEntity.ok(outgoingCallListResponse);
    }

    @GetMapping("/customer")
    public ResponseEntity<List<OutgoingRes>> outgoingReqList(OutgoingSearchReq outgoingSearchReq, @AuthenticationPrincipal CustomUserDetails userDetails){
        List<OutgoingRes> outgoingResList = outgoingService.getList(outgoingSearchReq, userDetails.getUser().getUserId());
        return ResponseEntity.ok(outgoingResList);
    }

    @GetMapping("/customer/{outgoingId}")
    public ResponseEntity<List<CustomerOutgoingDetailRes>> getOutgoingDetail(@PathVariable("outgoingId") Long outgoingId) {
        List<CustomerOutgoingDetailRes> customerOutgoingList = outgoingDetailService.getCustomerOutgoingList(outgoingId);
        return ResponseEntity.ok(customerOutgoingList);
    }

        @GetMapping("/{outgoingId}")
    public List<OutgoingDetailResponse> outgoingReqDetailList(@PathVariable("outgoingId") Long outgoingId){
        return outgoingDetailService.findByOutgoingAndStateIsAcitve(outgoingId).stream().map(OutgoingDetailResponse::from).collect(Collectors.toList());
    }

    @GetMapping("/customer/today")
    public ResponseEntity<OutgoingTodayRes> customerTodayInfoGet( @AuthenticationPrincipal CustomUserDetails userDetails){
        OutgoingTodayRes todayInfo = outgoingService.getTodayInfo(userDetails.getUser().getUserId());
        return ResponseEntity.ok(todayInfo);
    }

    @GetMapping("/recent")
    public ResponseEntity<OutgoingRes> recentlyOutgoingGet(@AuthenticationPrincipal CustomUserDetails userDetails){
        OutgoingRes recentlyOutgoing = outgoingService.getRecentlyOutgoing(userDetails.getUser().getUserId());
        return ResponseEntity.ok(recentlyOutgoing);
    }

    @PatchMapping("/{outgoingId}")
    public ResponseEntity<OutgoingCallDto> outgoingStateChange(@PathVariable("outgoingId") Long outgoingId, @RequestBody OutgoingStateChangeRequest request,@AuthenticationPrincipal CustomUserDetails userDetails) throws InterruptedException {

        OutgoingState state = OutgoingState.from(request.getState());
        OutgoingCallDto outgoing = OutgoingCallDto.from(outgoingLockFacadeService.changeOutgoingState(userDetails.getUser().getCompanyId(), outgoingId,state));
        return ResponseEntity.ok(outgoing);
    }

    @PutMapping("/{outgoingId}")
    public void outgoingDetailRecovery(@PathVariable("outgoingId")Long outgoingId){
        outgoingDetailService.updateByOutgoingDetailStateToActive(outgoingId);
    }

    @DeleteMapping("/details/{outgoingDetailId}")
    public void outgoingDetailDelete(@PathVariable("outgoingDetailId")Long detailId){
        outgoingDetailService.deleteOutgoingDetail(detailId);
    }
}
