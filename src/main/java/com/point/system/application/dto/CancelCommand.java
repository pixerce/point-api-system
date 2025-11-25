package com.point.system.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CancelCommand {
    private String orderId;
    private Long amount;
}
