package com.point.system.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PointRetrieveCommand {
    private String requestId;
    private Long userPointId;
}
