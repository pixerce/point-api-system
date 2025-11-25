package com.point.system.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class OrderCommand {
    private String orderId;
    private String userId;
    private Long amount;
}
