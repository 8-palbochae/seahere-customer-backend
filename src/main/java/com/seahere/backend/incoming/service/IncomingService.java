package com.seahere.backend.incoming.service;

import com.seahere.backend.incoming.controller.request.IncomingDataRequest;
import com.seahere.backend.incoming.controller.request.IncomingEditReq;

public interface IncomingService {
    void save(Long companyId, Long userId, IncomingDataRequest incomingDataRequest);
    Long editIncoming(IncomingEditReq incomingEditReq);
}