package com.point.system.application.dto;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class PointIssueResponse {
    private String requestId;
    private Long userPointId;
    private String message;
}
