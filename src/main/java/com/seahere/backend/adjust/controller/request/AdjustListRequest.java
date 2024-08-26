package com.seahere.backend.adjust.controller.request;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdjustListRequest {
    private Long companyId;
}
