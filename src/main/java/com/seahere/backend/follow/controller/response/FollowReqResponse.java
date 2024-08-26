package com.seahere.backend.follow.controller.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowReqResponse {
    private Long followId;
    private Long companyId;
    private String companyName;
}
