package com.seahere.backend.incoming.dto;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
@Builder
@NoArgsConstructor
public class IncomingCountDto {

    private LocalDate incomingDate;
    private long count;

}